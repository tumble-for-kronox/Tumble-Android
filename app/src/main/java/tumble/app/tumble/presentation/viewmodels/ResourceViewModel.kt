package tumble.app.tumble.presentation.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.data.api.auth.AuthManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.presentation.ResourceSelectionModel
import tumble.app.tumble.presentation.viewmodels.AccountViewModel.AuthStatus
import tumble.app.tumble.utils.isoDateFormatterDate
import tumble.app.tumble.utils.isoDateFormatterNoTimeZone
import tumble.app.tumble.utils.toIsoString
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

    fun setBookingDate(newDate: Date){
        _selectedPickerDate.value = newDate
        viewModelScope.launch { getAllResources(newDate) }
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
        viewModelScope.launch() {
            val refreshToken = authManager.getRefreshToken() ?: return@launch
            val schoolID = dataStoreManager.authSchoolId.value.toString()
            val endpoint = Endpoint.RegisterEvent(eventId = eventId, schoolId = schoolID)
            val response = kronoxManager.registerForEvent(endpoint, refreshToken)

            when(response){
                is ApiResponse.Error -> {
                    if(response.errorMessage == "Empty response body"){
                        Log.e("unbooked", "Success")
                    }else{
                        Log.e("unbooked", "Error")
                        Log.e("unbooked", response.errorMessage)
                    }
                }
                else -> {
                    Log.e("unbooked", "lmao")
                }
            }
        }
    }

    fun unregisterForEvent(eventId: String){
        viewModelScope.launch {

            val endpoint = Endpoint.UnregisterEvent(eventId, dataStoreManager.authSchoolId.value.toString())
            val refreshToken = authManager.getRefreshToken() ?: return@launch
            val response = kronoxManager.unRegisterForEvent(endpoint, refreshToken)

            when(response){
                is ApiResponse.Error -> {
                    if(response.errorMessage == "Empty response body"){
                        Log.e("unRegister", "Success")
                    }else{
                        Log.e("unRegister", "Error")
                        Log.e("unRegister", response.errorMessage)
                    }
                }
                else -> {
                    Log.e("unRegister", "lmao")
                }
            }
        }
    }

    fun getAllResources(date: Date = Date()){
        val dateString = isoDateFormatterDate.format(date)
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.AllResources(dataStoreManager.authSchoolId.value.toString(), dateString)
                val refreshToken = authManager.getRefreshToken() ?: return@launch
                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>> = kronoxManager.getAllResources(endpoint, refreshToken, null)

                when(response){
                    is ApiResponse.Success -> {
                        Log.e("getAllResourcesTest", "Success")
                        _allResources.value = response.data
                        _resourceBookingPageState.value = PageState.LOADED
                    }
                    is ApiResponse.Error -> {
                        Log.e("getAllResourcesTest", "Error")
                        Log.e("getAllResourcesTest", response.errorMessage)
                        _resourceBookingPageState.value = PageState.ERROR
                    }
                    else -> {}
                }
            }catch (e:Exception){
                Log.e("getAllResources", "Error")
                _resourceBookingPageState.value = PageState.ERROR
            }
        }
    }

    suspend fun bookResource(resourceId: String, date: Date, availabilityValue: NetworkResponse.AvailabilityValue): Boolean{
        val endpoint = Endpoint.BookResource(dataStoreManager.authSchoolId.value.toString())
        val resource = NetworkRequest.BookKronoxResource(resourceId, isoDateFormatterDate.format(date), availabilityValue)
        val refreshToken = authManager.getRefreshToken() ?: return false


        val response: ApiResponse<NetworkResponse.KronoxUserBookingElement> = kronoxManager.bookResource(endpoint, refreshToken, resource)
        when(response){
            is ApiResponse.Success ->{
                Log.e("BookedResource", "Success")
                return true
            }
            is ApiResponse.Error -> {
                if(response.errorMessage == "Empty response body") {
                    Log.e("BookedResource", "Success")
                }else{
                    Log.e("BookedResource", "Error")
                }
                return response.errorMessage == "Empty response body"
            }
            else -> {
                Log.e("BookedResource", "Error")
                return false
            }
        }
    }
}