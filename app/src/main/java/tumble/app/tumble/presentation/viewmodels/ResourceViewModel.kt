package tumble.app.tumble.presentation.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _allResourcesTypes = MutableStateFlow<List<NetworkResponse.KronoxResourceElement>?>(null)
    val allResourcesTypes: StateFlow<List<NetworkResponse.KronoxResourceElement>?> = _allResourcesTypes

    //val allResources: List<NetworkResponse.KronoxResourceElement>? = null

    private val _resourceSelectionModel = MutableStateFlow<ResourceSelectionModel?>(null)
    var resourceSelectionModel by mutableStateOf<ResourceSelectionModel?>(null)

    fun setBookingDate(newDate: Date){
        _selectedPickerDate.value = newDate
        viewModelScope.launch { getAllResourcesData(isoDateFormatterDate.format(newDate)) }
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
            try {
                val refreshToken = authManager.getRefreshToken() ?: return@launch
                val schoolID = dataStoreManager.authSchoolId.value.toString()
                val endpoint = Endpoint.RegisterEvent(eventId = eventId, schoolId = schoolID)
                val userRequest = kronoxManager.registerForEvent(endpoint, refreshToken)
                if(userRequest.isSuccessful){
                    Log.e("AAAAAAA", "Registered")
                }else{
                    Log.e("AAAAAAA", "NOT  Registered")

                }
            } catch (e: Exception) {
            }
        }
    }

    fun unregisterForEvent(eventId: String){
        //TODO
    }

    fun getAllResources(){
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.AllResources(dataStoreManager.authSchoolId.value.toString())
                val refreshToken = authManager.getRefreshToken() ?: return@launch
                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>> = kronoxManager.getAllResources(endpoint, refreshToken, null)

                when(response){
                    is ApiResponse.Success -> {
                        _allResourcesTypes.value = response.data
                        getAllResourcesData(date = isoDateFormatterDate.format(Date()))
                    }
                    is ApiResponse.Error -> {
                        _resourceBookingPageState.value = PageState.ERROR
                    }
                    else -> {}
                }
            }catch (e:Exception){
                _resourceBookingPageState.value = PageState.ERROR
            }
        }
    }

    fun getAllResourcesData(date: String){
        viewModelScope.launch {
            _resourceBookingPageState.value = PageState.LOADING

            val resources: MutableList<NetworkResponse.KronoxResourceElement> = mutableListOf()

            _allResourcesTypes.value?.forEach {
                it.id?.let { id ->
                    try {
                        val endpoint = Endpoint.AllResourceData(
                            dataStoreManager.authSchoolId.value.toString(),
                            resourceId = id,
                            date = date
                        )
                        val refreshToken = authManager.getRefreshToken() ?: return@launch
                        val response: ApiResponse<NetworkResponse.KronoxResourceElement> =
                            kronoxManager.getAllResourceData(endpoint, refreshToken, null)

                        when (response) {
                            is ApiResponse.Success -> {
                                resources.add(response.data)
                            }

                            is ApiResponse.Error -> {
                                _resourceBookingPageState.value = PageState.ERROR
                                return@launch
                            }

                            else -> {}
                        }
                    } catch (e: Exception) {
                        _resourceBookingPageState.value = PageState.ERROR
                        return@launch
                    }
                }
            }
            _allResources.value = resources.toList()
            _resourceBookingPageState.value = PageState.LOADED
        }
    }

    fun confirmResource(resourceId: String, bookingId: String){
        val endpoint = Endpoint.ConfirmResource(dataStoreManager.authSchoolId.value.toString())
        val resource = NetworkRequest.ConfirmKronoxResource(resourceId, bookingId)
        val refreshToken = authManager.getRefreshToken() ?: return

        viewModelScope.launch {
            val response = kronoxManager.confirmResource(endpoint, refreshToken, resource)
        }
    }

    fun bookResource(resourceId: String, date: Date, availabilityValue: NetworkResponse.AvailabilityValue){
        val endpoint = Endpoint.BookResource(dataStoreManager.authSchoolId.value.toString())
        val resource = NetworkRequest.BookKronoxResource(resourceId, isoDateFormatterDate.format(date), availabilityValue)
        val refreshToken = authManager.getRefreshToken() ?: return

        viewModelScope.launch {
            val response = kronoxManager.bookResource(endpoint, refreshToken, resource)
            if (response.isSuccessful){
                return@launch
            }
        }
    }

    fun unBookResource(bookingId: String){
        val endpoint = Endpoint.UnBookResource(dataStoreManager.authSchoolId.value.toString(), bookingId)
        val refreshToken = authManager.getRefreshToken() ?: return

        viewModelScope.launch {
            val response = kronoxManager.unBookResource(endpoint, refreshToken)
        }
    }
}