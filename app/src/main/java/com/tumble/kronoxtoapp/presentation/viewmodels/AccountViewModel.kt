package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.data.api.Endpoint
import com.tumble.kronoxtoapp.data.api.auth.AuthManager
import com.tumble.kronoxtoapp.data.notifications.NotificationManager
import com.tumble.kronoxtoapp.data.repository.preferences.DataStoreManager
import com.tumble.kronoxtoapp.datasource.SchoolManager
import com.tumble.kronoxtoapp.datasource.network.ApiResponse
import com.tumble.kronoxtoapp.datasource.network.kronox.KronoxRepository
import com.tumble.kronoxtoapp.domain.models.TumbleUser
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.KronoxUserBookingElement
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.AvailableKronoxUserEvent
import com.tumble.kronoxtoapp.domain.models.presentation.School
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val kronoxManager: KronoxRepository,
    private val dataStoreManager: DataStoreManager,
    private val schoolManager: SchoolManager,
    private val notificationManager: NotificationManager
) : ViewModel() {
    private val _authStatus = MutableStateFlow(AuthStatus.LOADING)
    val authStatus: StateFlow<AuthStatus> = _authStatus

    private val _user = MutableStateFlow<TumbleUser?>(null)
    val user: StateFlow<TumbleUser?> = _user

    private val _refreshToken = MutableStateFlow<String?>(null)
    private val refreshToken: StateFlow<String?> = _refreshToken

    private val _sessionDetails = MutableStateFlow<String?>(null)
    private val sessionDetails: StateFlow<String?> = _sessionDetails

    private val _registeredEventSectionState = MutableStateFlow<PageState>(PageState.LOADING)
    val registeredEventSectionState: StateFlow<PageState> = _registeredEventSectionState

    private val _registeredBookingsSectionState = MutableStateFlow<PageState>(PageState.LOADING)
    val registeredBookingsSectionState: StateFlow<PageState> = _registeredBookingsSectionState

    private val _userBookings = MutableStateFlow<NetworkResponse.KronoxUserBookings?>(null)
    val userBookings: StateFlow<NetworkResponse.KronoxUserBookings?> = _userBookings

    private val _completeUserEvent = MutableStateFlow<NetworkResponse.KronoxCompleteUserEvent?>(null)
    val completeUserEvent: StateFlow<NetworkResponse.KronoxCompleteUserEvent?> = _completeUserEvent

    private val _attemptingLogin = MutableStateFlow<Boolean>(false)
    val attemptingLogin: StateFlow<Boolean> = _attemptingLogin

    private val _booking = MutableStateFlow<KronoxUserBookingElement?>(null)
    val booking: StateFlow<KronoxUserBookingElement?> = _booking

    private val _event = MutableStateFlow<AvailableKronoxUserEvent?>(null)
    val event: StateFlow<AvailableKronoxUserEvent?> = _event

    fun setEvent(event: AvailableKronoxUserEvent){
        _event.value = event
    }

    fun unSetEvent(){
        _event.value = null
    }

    fun setBooking(booking: KronoxUserBookingElement){
        _booking.value = booking
    }

    fun unSetBooking(){
        _booking.value = null
    }
    var isSigningOut = mutableStateOf(false)

    init {
        viewModelScope.launch {
            autoLogin()
        }
    }

    fun openLogOutConfirm() {
        isSigningOut.value = true
    }

    fun closeLogOutConfirm() {
        isSigningOut.value = false
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authManager.logOutUser()
                _authStatus.value = AuthStatus.UNAUTHORIZED
            } catch (e: Exception) {
                // TODO: Handle logout error
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _attemptingLogin.value = true
                val userRequest = NetworkRequest.KronoxUserLogin(username, password)
                val user = authManager.loginUser(userRequest)
                updateUser(user)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.UNAUTHORIZED
            } finally {
                _attemptingLogin.value = false
            }
        }
    }

    fun getSchoolName(): School?{
        return schoolManager.getSchoolById(dataStoreManager.authSchoolId.value)
    }

    fun getUserEventsForSection() {
        viewModelScope.launch(Dispatchers.IO) {
            _registeredEventSectionState.value = PageState.LOADING
            val endpoint = Endpoint.UserEvents(dataStoreManager.authSchoolId.value.toString())
            if (refreshToken.value != null) {
                val response: ApiResponse<NetworkResponse.KronoxCompleteUserEvent> =
                    kronoxManager.getKronoxCompleteUserEvent(endpoint, refreshToken.value, sessionDetails.value)
                when (response) {
                    is ApiResponse.Success -> {
                        _completeUserEvent.value = response.data
                        _registeredEventSectionState.value = PageState.LOADED
                    }
                    else -> {
                        _registeredEventSectionState.value = PageState.ERROR
                    }
                }
            }
        }
    }

    fun getUserBookingsForSection() {
        viewModelScope.launch(Dispatchers.IO) {
            _registeredBookingsSectionState.value = PageState.LOADING
            val endpoint = Endpoint.UserBookings(dataStoreManager.authSchoolId.value.toString())
            if (refreshToken.value != null) {

                val response: ApiResponse<List<KronoxUserBookingElement>> =
                    kronoxManager.getKronoxUserBookings(endpoint, refreshToken.value, sessionDetails.value)

                when (response) {
                    is ApiResponse.Success -> {
                        _userBookings.value = NetworkResponse.KronoxUserBookings(bookings = response.data)
                        _registeredBookingsSectionState.value = PageState.LOADED
                        scheduleBookingNotifications(_userBookings)
                    }
                    is ApiResponse.Error -> {
                        _registeredBookingsSectionState.value = PageState.ERROR
                    }
                    else -> {
                        _registeredBookingsSectionState.value = PageState.ERROR
                    }
                }
            }
        }
    }

    fun unBookResource(bookingId: String){
        val endpoint = Endpoint.UnBookResource(dataStoreManager.authSchoolId.value.toString(), bookingId)
        val refreshToken = authManager.getRefreshToken() ?: return

        // This logic is completely cursed and should be fixed in the backend
        viewModelScope.launch {
            when(val response = kronoxManager.unBookResource(endpoint, refreshToken)) {
                is ApiResponse.Error -> {
                    if(response.errorMessage == "Empty response body"){
                        Log.e("AccountViewModel", "Success")
                    } else{
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

    fun confirmResource(resourceId: String, bookingId: String){
        val endpoint = Endpoint.ConfirmResource(dataStoreManager.authSchoolId.value.toString())
        val resource = NetworkRequest.ConfirmKronoxResource(resourceId, bookingId)
        val refreshToken = authManager.getRefreshToken() ?: return

        // This logic is completely cursed and should be fixed in the backend
        viewModelScope.launch {
            when(val response = kronoxManager.confirmResource(endpoint, refreshToken, resource)){
                is ApiResponse.Error -> {
                    if(response.errorMessage == "Empty response body"){
                        Log.e("confirm", "Success")
                    }else{
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

    private fun scheduleBookingNotifications(userBookings: MutableStateFlow<NetworkResponse.KronoxUserBookings?>) {
        userBookings.let {
            userBookings.value?.bookings?.forEach {
                if (!notificationManager.isNotificationScheduled(it.id)) {
                    notificationManager.createNotificationFromBooking(it)
                }
            }
        }
    }

    private fun updateUser(user: TumbleUser) {
        _user.value = user
        _refreshToken.value = authManager.getRefreshToken()
        _authStatus.value = AuthStatus.AUTHORIZED
    }

    private fun autoLogin() {
        viewModelScope.launch {
            try {
                val user = authManager.autoLoginUser()
                updateUser(user)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.UNAUTHORIZED
            }
        }
    }

    enum class AuthStatus {
        AUTHORIZED, UNAUTHORIZED, LOADING
    }
}
