package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events.EventType
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountEventsState {
    data object Loading : AccountEventsState()
    data class Loaded(val events: NetworkResponse.KronoxCompleteUserEvent) : AccountEventsState()
    data class Error(val message: String) : AccountEventsState()
}

@HiltViewModel
class AccountEventsViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val dataStoreService: DataStoreService,
    private val kronoxService: KronoxService
): ViewModel() {

    var state by mutableStateOf<AccountEventsState>(AccountEventsState.Loading)

    fun getAllEvents(){
        viewModelScope.launch {
            with (Dispatchers.Main) {
                state = AccountEventsState.Loading
            }
            try {
                val endpoint = Endpoint.UserEvents(dataStoreService.authSchoolId.value.toString())
                val refreshToken = authenticationService.getRefreshToken() ?: return@launch

                val response: ApiResponse<NetworkResponse.KronoxCompleteUserEvent> =
                    kronoxService.getKronoxCompleteUserEvent(endpoint, refreshToken, sessionDetails = null)

                when(response){
                    is ApiResponse.Success -> {
                        with (Dispatchers.Main) {
                            state = AccountEventsState.Loaded(response.data)
                        }
                    }
                    is ApiResponse.Error -> {
                        with (Dispatchers.Main) {
                            state = AccountEventsState.Error(response.errorMessage)
                        }
                    }
                }
            } catch (e:Exception) {
                with (Dispatchers.Main) {
                    state = AccountEventsState.Error(e.localizedMessage ?: "Unknown error occurred")
                }
            }
        }
    }

    private fun registerForEvent(eventId: String) {
        viewModelScope.launch() {
            val refreshToken = authenticationService.getRefreshToken() ?: return@launch
            val schoolID = dataStoreService.authSchoolId.value.toString()
            val endpoint = Endpoint.RegisterEvent(eventId = eventId, schoolId = schoolID)
            when (val response = kronoxService.registerForEvent(endpoint, refreshToken)) {
                is ApiResponse.Error -> {
                    if (response.errorMessage == "Empty response body") {
                        Log.d("AccountEventsViewModel", "Success")
                    } else {
                        Log.e("AccountEventsViewModel", "Error")
                        Log.e("AccountEventsViewModel", response.errorMessage)
                    }
                } // We cannot get Success for some reason
                else -> {
                    Log.e("AccountEventsViewModel", "Should not be possible")
                }
            }
        }
    }

    private fun unregisterForEvent(eventId: String){
        viewModelScope.launch {

            val endpoint = Endpoint.UnregisterEvent(eventId, dataStoreService.authSchoolId.value.toString())
            val refreshToken = authenticationService.getRefreshToken() ?: return@launch
            when(val response = kronoxService.unRegisterForEvent(endpoint, refreshToken)){
                is ApiResponse.Error -> {
                    if(response.errorMessage == "Empty response body"){
                        Log.d("AccountEventsViewModel", "Success")
                    }else{
                        Log.e("AccountEventsViewModel", "Error")
                        Log.e("AccountEventsViewModel", response.errorMessage)
                    }
                }
                else -> {
                    Log.e("AccountEventsViewModel", "Should not be here")
                }
            }
        }
    }

    fun bookingAction(eventId: String, eventType: EventType) {
        when (eventType) {
            EventType.REGISTER -> {
                registerForEvent(eventId)
            }
            EventType.UNREGISTER -> {
                unregisterForEvent(eventId)
            }
        }
    }

}