package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.presentation.School
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val schoolManager: SchoolManager,
    private val authManager: AuthManager
): ViewModel(){

    private val _authSchoolId = MutableStateFlow(-1)
    val authSchoolId: StateFlow<Int> = _authSchoolId

    val schools: List<School> = schoolManager.getSchools()

    init {
        setupDatePublishers()
    }

    private fun setupDatePublishers(){
        viewModelScope.launch {
            dataStoreManager.authSchoolId.collect{id ->
                _authSchoolId.value = id
            }
        }
    }

    fun setDefaultAuthSchool(schoolId: Int){
        viewModelScope.launch {
            dataStoreManager.setAuthSchoolId(id = schoolId)
        }
    }

    fun getSchoolName(): School?{
        return schoolManager.getSchoolById(authSchoolId.value)
    }
}