package tumble.app.tumble.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.presentation.ResourceSelectionModel
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class ResourceViewModel @Inject constructor(
    val authManager: AuthManager,
    val dataStoreManager: DataStoreManager,
    val kronoxManager: KronoxRepository

): ViewModel(){

    private val _selectedPickerDate = MutableStateFlow<Date>(Date())
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedPickerDate: StateFlow<Date> = _selectedPickerDate

    private val _completeUserEvent = MutableStateFlow<NetworkResponse.KronoxCompleteUserEvent?>(null)
    val completeUserEvent: StateFlow<NetworkResponse.KronoxCompleteUserEvent?> = _completeUserEvent

    private val _resourceBookingPageState = MutableStateFlow<PageState>(PageState.LOADING)
    val resourceBookingPageState: StateFlow<PageState> = _resourceBookingPageState

    private val _eventBookingPageState = MutableStateFlow<PageState>(PageState.LOADING)
    val eventBookingPageState: StateFlow<PageState> = _eventBookingPageState


    private val _allResources = MutableStateFlow<List<NetworkResponse.KronoxResourceElement>?>(null)
    val allResources: StateFlow<List<NetworkResponse.KronoxResourceElement>?> = _allResources

    //val allResources: List<NetworkResponse.KronoxResourceElement>? = null

    private val _resourceSelectionModel = MutableStateFlow<ResourceSelectionModel?>(null)
    var resourceSelectionModel by mutableStateOf<ResourceSelectionModel?>(null)


    fun setBookingDate(newDate: Date){
        _selectedPickerDate.value = newDate
    }

    fun getUserEventsForPage(){
        viewModelScope.launch {
            _eventBookingPageState.value = PageState.LOADING
            try {
                val endpoint = Endpoint.UserEvents(dataStoreManager.authSchoolId.value.toString())
                val refreshToken = authManager.getRefreshToken() ?: return@launch

                val response: ApiResponse<NetworkResponse.KronoxCompleteUserEvent> =
                    kronoxManager.getKronoxCompleteUserEvent(endpoint, refreshToken, sessionDetails = null)

                when(response){
                    is ApiResponse.Success -> {
                        _completeUserEvent.value = response.data
                        _eventBookingPageState.value = PageState.LOADED
                    }
                    else -> {}
                }
            }catch (e:Exception){
                _eventBookingPageState.value = PageState.ERROR
            }
        }
    }

    fun registerForEvent(eventId: String){
        //TODO
    }

    fun unregisterForEvent(eventId: String){
        //TODO
    }

    fun getAllResourceData(date: Date){
        _resourceBookingPageState.value = PageState.LOADING
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.AllResources(dataStoreManager.authSchoolId.toString(), date = date)
                val refreshToken = authManager.getRefreshToken() ?: return@launch

                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>> = kronoxManager.getAllResources(endpoint, refreshToken, sessionDetails = null)
                when(response){
                    is ApiResponse.Success -> {
                        _allResources.value = response.data
                        _resourceBookingPageState.value = PageState.LOADED
                    }
                    else -> {
                        _resourceBookingPageState.value = PageState.ERROR
                    }
                }
            }catch (e:Exception){
                _resourceBookingPageState.value = PageState.ERROR
            }
            }
        }


    fun confirmResource(resourceId: String, bookingId: String){
        //TODO
    }

    fun bookResource(resourceId: String, date: Date, availabilityValue: NetworkResponse.AvailabilityValue):Boolean{
        //TODO
        return false
    }

    fun unbookResource(bookingId: String): Boolean{
        //TODO
        return true
    }

}