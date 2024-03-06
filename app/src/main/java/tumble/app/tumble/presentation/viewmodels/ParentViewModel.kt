package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.datasource.preferences.DataStoreManager
import tumble.app.tumble.datasource.realm.RealmManager
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

data class CombinedData(val authSchoolId: Int, val userOnBoarded: Boolean)

@HiltViewModel
class ParentViewModel @Inject constructor(
    private val kronoxManager: KronoxRepository,
    private val realmManager: RealmManager,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private val _combinedData = MutableStateFlow(CombinedData(-1, false))
    val combinedData: StateFlow<CombinedData> = _combinedData

    init {
        observeDataStoreChanges()
    }

    private fun observeDataStoreChanges() {
        viewModelScope.launch {
            dataStoreManager.authSchoolId.combine(dataStoreManager.userOnBoarded) { authSchoolId, userOnBoarded ->
                CombinedData(authSchoolId, userOnBoarded)
            }.collect { combinedData ->
                _combinedData.value = combinedData
            }
        }
    }
}
