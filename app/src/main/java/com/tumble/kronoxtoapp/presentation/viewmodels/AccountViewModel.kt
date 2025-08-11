package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import com.tumble.kronoxtoapp.services.notifications.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountDataState {
    data object Idle : AccountDataState()
    data object Loading : AccountDataState()
    data class EventsLoaded(val events: NetworkResponse.KronoxCompleteUserEvent) :
        AccountDataState()

    data class BookingsLoaded(val bookings: NetworkResponse.KronoxUserBookings) : AccountDataState()
    data class Error(val message: String) : AccountDataState()
}

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val kronoxManager: KronoxService,
    private val dataStoreService: DataStoreService,
    private val schoolService: SchoolService,
    private val notificationService: NotificationService
) : ViewModel() {

    private val _eventsState = MutableStateFlow<AccountDataState>(AccountDataState.Idle)
    val eventsState: StateFlow<AccountDataState> = _eventsState.asStateFlow()

    private val _bookingsState = MutableStateFlow<AccountDataState>(AccountDataState.Idle)
    val bookingsState: StateFlow<AccountDataState> = _bookingsState.asStateFlow()

    private val _isShowingLogoutDialog = MutableStateFlow(false)
    val isShowingLogoutDialog: StateFlow<Boolean> = _isShowingLogoutDialog.asStateFlow()

    // Selected items for detail sheet views
    private val _selectedBooking = MutableStateFlow<NetworkResponse.KronoxUserBookingElement?>(null)
    val selectedBooking = _selectedBooking.asStateFlow()

    private val _selectedEvent = MutableStateFlow<NetworkResponse.AvailableKronoxUserEvent?>(null)
    val selectedEvent = _selectedEvent.asStateFlow()

    val authState = authenticationService.authState

    fun getSchoolName(): School? {
        return schoolService.getSchoolById(dataStoreService.authSchoolId.value)
    }

    fun loadUserEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _eventsState.value = AccountDataState.Loading

            try {
                val endpoint = Endpoint.UserEvents(dataStoreService.authSchoolId.value.toString())
                val refreshToken = authenticationService.getRefreshToken()

                if (refreshToken == null) {
                    _eventsState.value = AccountDataState.Error("Authentication required")
                    return@launch
                }

                when (val response =
                    kronoxManager.getKronoxCompleteUserEvent(endpoint, refreshToken, null)) {
                    is ApiResponse.Success -> {
                        _eventsState.value = AccountDataState.EventsLoaded(response.data)
                    }

                    is ApiResponse.Error -> {
                        _eventsState.value = AccountDataState.Error(response.errorMessage)
                    }
                }
            } catch (e: Exception) {
                _eventsState.value =
                    AccountDataState.Error(e.localizedMessage ?: "Failed to load events")
            }
        }
    }

    fun loadUserBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            _bookingsState.value = AccountDataState.Loading

            try {
                val endpoint = Endpoint.UserBookings(dataStoreService.authSchoolId.value.toString())
                val refreshToken = authenticationService.getRefreshToken()

                if (refreshToken == null) {
                    _bookingsState.value = AccountDataState.Error("Authentication required")
                    return@launch
                }

                when (val response =
                    kronoxManager.getKronoxUserBookings(endpoint, refreshToken, null)) {
                    is ApiResponse.Success -> {
                        val bookings = NetworkResponse.KronoxUserBookings(bookings = response.data)
                        _bookingsState.value = AccountDataState.BookingsLoaded(bookings)
                        scheduleBookingNotifications(response.data)
                    }

                    is ApiResponse.Error -> {
                        _bookingsState.value = AccountDataState.Error(response.errorMessage)
                    }
                }
            } catch (e: Exception) {
                _bookingsState.value =
                    AccountDataState.Error(e.localizedMessage ?: "Failed to load bookings")
            }
        }
    }

    fun unbookResource(bookingId: String) {
        val endpoint =
            Endpoint.UnBookResource(dataStoreService.authSchoolId.value.toString(), bookingId)
        val refreshToken = authenticationService.getRefreshToken() ?: return

        // This logic is completely cursed and should be fixed in the backend
        viewModelScope.launch {
            when (val response = kronoxManager.unBookResource(endpoint, refreshToken)) {
                is ApiResponse.Error -> {
                    if (response.errorMessage == "Empty response body") {
                        Log.e("AccountViewModel", "Success")
                    } else {
                        Log.e("AccountViewModel", "Error")
                        Log.e("AccountViewModel", response.errorMessage)
                    }
                }

                else -> {
                    Log.e("AccountViewModel", "We should not be here")

                }
            }
        }
    }

    fun confirmResource(resourceId: String, bookingId: String) {
        val endpoint = Endpoint.ConfirmResource(dataStoreService.authSchoolId.value.toString())
        val resource = NetworkRequest.ConfirmKronoxResource(resourceId, bookingId)
        val refreshToken = authenticationService.getRefreshToken() ?: return

        // This logic is completely cursed and should be fixed in the backend
        viewModelScope.launch {
            when (val response = kronoxManager.confirmResource(endpoint, refreshToken, resource)) {
                is ApiResponse.Error -> {
                    if (response.errorMessage == "Empty response body") {
                        Log.d("confirm", "Success")
                    } else {
                        Log.e("confirm", "Error")
                        Log.e("confirm", response.errorMessage)
                    }
                }

                else -> {
                    Log.e("confirm", "Error")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticationService.logOutUser()
            _isShowingLogoutDialog.value = false
        }
    }

    fun showLogoutDialog() {
        _isShowingLogoutDialog.value = true
    }

    fun hideLogoutDialog() {
        _isShowingLogoutDialog.value = false
    }

    fun selectBooking(booking: NetworkResponse.KronoxUserBookingElement) {
        _selectedBooking.value = booking
    }

    fun clearSelectedBooking() {
        _selectedBooking.value = null
    }

    fun selectEvent(event: NetworkResponse.AvailableKronoxUserEvent) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }

    private fun scheduleBookingNotifications(bookings: List<NetworkResponse.KronoxUserBookingElement>) {
        bookings.forEach { booking ->
            if (!notificationService.isNotificationScheduled(booking.id)) {
                notificationService.createNotificationFromBooking(booking)
            }
        }
    }
}