package tumble.app.tumble.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.domain.models.TumbleUser
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NetworkResponse.KronoxUserBookingElement
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.network.NetworkResponse.AvailableKronoxUserEvent
import tumble.app.tumble.domain.models.presentation.School
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val kronoxManager: KronoxRepository,
    private val dataStoreManager: DataStoreManager,
    private val schoolManager: SchoolManager
) : ViewModel() {
    private val _authStatus = MutableStateFlow(AuthStatus.UNAUTHORIZED)
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

    fun autoSignUpEnabled(): Boolean{
        return dataStoreManager.autoSignup.value
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

    fun logIn(authSchoolId: Int, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _attemptingLogin.value = true
                val userRequest = NetworkRequest.KronoxUserLogin(username, password)
                val user = authManager.loginUser(userRequest)
                updateUser(user)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.UNAUTHORIZED
            }finally {
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
                        scheduleBookingNotifications()
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

        viewModelScope.launch {
            val response = kronoxManager.unBookResource(endpoint, refreshToken)
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

    fun confirmResource(resourceId: String, bookingId: String){
        val endpoint = Endpoint.ConfirmResource(dataStoreManager.authSchoolId.value.toString())
        val resource = NetworkRequest.ConfirmKronoxResource(resourceId, bookingId)
        val refreshToken = authManager.getRefreshToken() ?: return

        viewModelScope.launch {
            val response = kronoxManager.confirmResource(endpoint, refreshToken, resource)
            when(response){
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


//    fun getUserEventsForSection() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _registeredEventSectionState.value = PageState.LOADING
//            val endpoint: Endpoint = Endpoint.UserEvents(combinedData.value.authSchoolId.toString())
//            if (refreshToken.value != null) {
//                val response: ApiResponse<NetworkResponse.KronoxCompleteUserEvent> =
//                    kronoxManager.get(endpoint, refreshToken.value, sessionDetails.value)
//
//                when (response) {
//                    is ApiResponse.Success -> {
//
//                        _completeUserEvent.value = response.data
//                        _registeredEventSectionState.value = PageState.LOADED
//                    }
//
//                    else -> {
//                        _registeredEventSectionState.value = PageState.ERROR
//                    }
//                }
////                if (response.isSuccessful) {
////                    response.body()?.let {
////                        val userEvents = it as NetworkResponse.KronoxCompleteUserEvent
////                        _completeUserEvent.value = userEvents
////                        _registeredEventSectionState.value = PageState.LOADED
////                    } ?: run {
////                        _registeredEventSectionState.value = PageState.ERROR
////                    }
////                } else {
////                    _registeredEventSectionState.value = PageState.ERROR
////                }
////            } else {
////                _registeredEventSectionState.value = PageState.ERROR
////            }
//            }
//        }
//    }



//    fun unregisterForEvent(eventId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val endpoint = Endpoint.UnregisterEvent(eventId, combinedData.value.authSchoolId.toString())
//            if (refreshToken.value != null) {
//                val response: ApiResponse<NetworkResponse.> = kronoxManager.put(endpoint, refreshToken.value, sessionDetails.value, null)
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        getUserEventsForSection()
//                    }
//                }
//            } else {
//                _registeredEventSectionState.value = PageState.ERROR
//            }
//        }
//    }

    fun toggleAutoSignup(value: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setAutoSignup(value)
        }
    }

    private fun scheduleBookingNotifications() {
        // Implement the logic for scheduling notifications
    }

    private fun registerAutoSignup() {
        val endpoint = Endpoint.RegisterAllEvents(dataStoreManager.authSchoolId.value.toString())
        val refreshToken = authManager.getRefreshToken() ?: return
        viewModelScope.launch {
            val response = kronoxManager.registerForAllEvents(endpoint, refreshToken)
            when (response) {
                is ApiResponse.Success -> {
                    Log.e("AutoSignup", "Success")
                }
                is ApiResponse.Error -> {
                    Log.e("AutoSignup", "Error")
                    Log.e("AutoSignup", response.errorMessage)
                }
                else -> {
                    Log.e("AutoSignup", "Error")
                }
            }
            // Implement the logic for automatic event registration
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
        AUTHORIZED, UNAUTHORIZED
    }
}
