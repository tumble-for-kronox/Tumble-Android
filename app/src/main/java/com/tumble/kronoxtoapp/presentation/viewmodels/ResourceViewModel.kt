package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.utils.isoDateFormatterDate
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class ResourceViewModel @Inject constructor(
    val authenticationService: AuthenticationService,
    val dataStoreService: DataStoreService,
    val kronoxManager: KronoxService
): ViewModel(){

    private val _selectedPickerDate = MutableStateFlow<Date>(Date())
    
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
                val endpoint = Endpoint.UserEvents(dataStoreService.authSchoolId.value.toString())
                val refreshToken = authenticationService.getRefreshToken() ?: return@launch

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
            val refreshToken = authenticationService.getRefreshToken() ?: return@launch
            val schoolID = dataStoreService.authSchoolId.value.toString()
            val endpoint = Endpoint.RegisterEvent(eventId = eventId, schoolId = schoolID)
            when(val response = kronoxManager.registerForEvent(endpoint, refreshToken)){
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

            val endpoint = Endpoint.UnregisterEvent(eventId, dataStoreService.authSchoolId.value.toString())
            val refreshToken = authenticationService.getRefreshToken() ?: return@launch
            when(val response = kronoxManager.unRegisterForEvent(endpoint, refreshToken)){
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
        _resourceBookingPageState.value = PageState.LOADING
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.AllResources(dataStoreService.authSchoolId.value.toString(), dateString)
                val refreshToken = authenticationService.getRefreshToken() ?: return@launch
                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>> = kronoxManager.getAllResources(endpoint, refreshToken, null)

                when(response){
                    is ApiResponse.Success -> {
                        Log.d("ResourceViewModel", "Success")
                        _allResources.value = response.data
                        _resourceBookingPageState.value = PageState.LOADED
                    }
                    is ApiResponse.Error -> {
                        Log.e("ResourceViewModel", "Error")
                        Log.e("ResourceViewModel", response.errorMessage)
                        _resourceBookingPageState.value = PageState.ERROR
                    }
                    else -> {}
                }
            }catch (e:Exception){
                Log.e("ResourceViewModel", "Error")
                _resourceBookingPageState.value = PageState.ERROR
            }
        }
    }

    suspend fun bookResource(resourceId: String, date: Date, availabilityValue: NetworkResponse.AvailabilityValue): Boolean{
        val endpoint = Endpoint.BookResource(dataStoreService.authSchoolId.value.toString())
        val resource = NetworkRequest.BookKronoxResource(resourceId, isoDateFormatterDate.format(date), availabilityValue)
        val refreshToken = authenticationService.getRefreshToken() ?: return false


        val response: ApiResponse<NetworkResponse.KronoxUserBookingElement> = kronoxManager.bookResource(endpoint, refreshToken, resource)
        when(response){
            is ApiResponse.Success ->{
                Log.e("ResourceViewModel", "Success")
                return true
            }
            is ApiResponse.Error -> {
                if(response.errorMessage == "Empty response body") {
                    Log.e("ResourceViewModel", "Success")
                } else{
                    Log.e("ResourceViewModel", "Error: ${response.errorMessage}")
                }
                return response.errorMessage == "Empty response body"
            }
            else -> {
                Log.e("ResourceViewModel", "Error")
                return false
            }
        }
    }
}