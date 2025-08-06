package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import com.tumble.kronoxtoapp.utils.isoDateFormatter
import com.tumble.kronoxtoapp.utils.isoDateFormatterDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

sealed class ResourceBookingState {
    data object Loading : ResourceBookingState()
    data class Loaded(val resource: NetworkResponse.KronoxResourceElement) : ResourceBookingState()
    data class Error(val message: String) : ResourceBookingState()
}

sealed class ResourceSelectionState {
    data object Loading : ResourceSelectionState()
    data class Loaded(val allResources: List<NetworkResponse.KronoxResourceElement>) : ResourceSelectionState()
    data class Error(val message: String) : ResourceSelectionState()
}

@HiltViewModel
class ResourceSelectionViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val dataStoreService: DataStoreService,
    private val kronoxService: KronoxService
): ViewModel() {

    var resourceBookingState by mutableStateOf<ResourceBookingState>(ResourceBookingState.Loading)
    var resourceSelectionState by mutableStateOf<ResourceSelectionState>(ResourceSelectionState.Loading)

    fun getAllResources(date: Date = Date()) {
        val dateString = isoDateFormatterDate.format(date)
        resourceSelectionState = ResourceSelectionState.Loading
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.AllResources(
                    dataStoreService.authSchoolId.value.toString(),
                    dateString
                )
                val refreshToken = authenticationService.getRefreshToken() ?: return@launch
                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>> =
                    kronoxService.getAllResources(endpoint, refreshToken, null)

                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("ResourceViewModel", "Success")
                        resourceSelectionState = ResourceSelectionState.Loaded(response.data)
                    }

                    is ApiResponse.Error -> {
                        Log.e("ResourceViewModel", "Error")
                        Log.e("ResourceViewModel", response.errorMessage)
                        resourceSelectionState = ResourceSelectionState.Error(response.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Log.e("ResourceViewModel", "Error")
                resourceSelectionState =
                    ResourceSelectionState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun getResource(resourceId: String, selectedPickerDate: Date) {
        resourceBookingState = ResourceBookingState.Loading
        viewModelScope.launch {
            try {
                val dateString = isoDateFormatter.format(selectedPickerDate)
                val endpoint = Endpoint.AllResources(dataStoreService.authSchoolId.value.toString(), dateString)
                val refreshToken = authenticationService.getRefreshToken()
                val response: ApiResponse<List<NetworkResponse.KronoxResourceElement>>
                    = kronoxService.getAllResources(endpoint, refreshToken, null)

                when(response){
                    is ApiResponse.Success -> {
                        Log.d("ResourceSelectionViewModel", "Success")
                        val allResources = response.data
                        val resource = allResources.find { it.id == resourceId }
                        with (Dispatchers.Main) {
                            resourceBookingState = resource?.let { ResourceBookingState.Loaded(it) } ?: ResourceBookingState.Error("Unknown error occurred")
                        }
                    }
                    is ApiResponse.Error -> {
                        Log.e("ResourceSelectionViewModel", "Error")
                        Log.e("ResourceSelectionViewModel", response.errorMessage)
                        with (Dispatchers.Main) {
                            resourceBookingState = ResourceBookingState.Error(response.errorMessage)
                        }
                    }
                }
            } catch (e:Exception) {
                Log.e("ResourceSelectionViewModel", "Error")
                with (Dispatchers.Main) {
                    resourceBookingState = e.localizedMessage?.let { ResourceBookingState.Error(it) } ?: ResourceBookingState.Error("Unknown error occurred")
                }
            }
        }
    }

    suspend fun bookResource(resourceId: String, date: Date, availabilityValue: NetworkResponse.AvailabilityValue): Boolean{
        val endpoint = Endpoint.BookResource(dataStoreService.authSchoolId.value.toString())
        val resource = NetworkRequest.BookKronoxResource(resourceId, isoDateFormatterDate.format(date), availabilityValue)
        val refreshToken = authenticationService.getRefreshToken() ?: return false


        val response: ApiResponse<NetworkResponse.KronoxUserBookingElement> = kronoxService.bookResource(endpoint, refreshToken, resource)
        when(response){
            is ApiResponse.Success ->{
                Log.e("ResourceSelectionViewModel", "Success")
                return true
            }
            is ApiResponse.Error -> {
                if(response.errorMessage == "Empty response body") {
                    Log.e("ResourceSelectionViewModel", "Success")
                } else{
                    Log.e("ResourceSelectionViewModel", "Error: ${response.errorMessage}")
                }
                return response.errorMessage == "Empty response body"
            }
            else -> {
                Log.e("ResourceSelectionViewModel", "Error")
                return false
            }
        }
    }

}