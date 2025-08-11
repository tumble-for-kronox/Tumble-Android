package com.tumble.kronoxtoapp.services.authentication

import com.tumble.kronoxtoapp.domain.models.TumbleUser
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.TokenType
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SecureStorageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: TumbleUser) : AuthState()
}

sealed class AuthError(message: String) : Exception(message) {
    data object HttpResponseError : AuthError("HTTP Response Error")
    data object TokenError : AuthError("Token Error")
    data object DecodingError : AuthError("Decoding Error")
    data object RequestError : AuthError("Request Error")
}

@Singleton
class AuthenticationService @Inject constructor(
    private val authenticationServiceProtocol: AuthenticationServiceProtocol,
    private val secureStorageService: SecureStorageService,
    private val dataStoreService: DataStoreService
) {
    private val secureStorageAccount = "kronoxtoapp-for-kronox"

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        serviceScope.launch {
            attemptAutoLogin()
        }
    }

    suspend fun loginUser(user: NetworkRequest.KronoxUserLogin): Result<TumbleUser> {
        return try {
            val authSchoolId = dataStoreService.authSchoolId.value.toString()
            val response = authenticationServiceProtocol.loginUser(authSchoolId, user)

            if (response.isSuccessful) {
                response.body()?.let {
                    storeTokens(
                        it.refreshToken,
                        "{\"sessionToken\":\"${it.sessionDetails.sessionToken}\",\"sessionLocation\":\"${it.sessionDetails.sessionLocation}\"}"
                    )
                    val tumbleUser = TumbleUser(username = it.username, name = it.name)
                    _authState.value = AuthState.Authenticated(tumbleUser)
                    Result.success(tumbleUser)
                } ?: Result.failure(AuthError.DecodingError)
            } else {
                _authState.value = AuthState.Unauthenticated
                Result.failure(AuthError.HttpResponseError)
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
            Result.failure(e)
        }
    }

    private suspend fun attemptAutoLogin() {
        try {
            val user = performAutoLogin()
            _authState.value = AuthState.Authenticated(user)
        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    suspend fun autoLoginUser(): TumbleUser {
        return performAutoLogin()
    }

    private suspend fun performAutoLogin(): TumbleUser {
        val authSchoolId = dataStoreService.authSchoolId.value.toString()
        val refreshToken =
            secureStorageService.read(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
                ?: throw AuthError.TokenError
        val sessionDetails =
            secureStorageService.read(TokenType.SESSION_DETAILS.name, secureStorageAccount)
                ?: throw AuthError.TokenError

        val response =
            authenticationServiceProtocol.autoLoginUser(authSchoolId, refreshToken, sessionDetails)

        if (response.isSuccessful) {
            storeHeaderTokens(response.headers())
            return response.body()?.let {
                TumbleUser(username = it.username, name = it.name)
            } ?: throw AuthError.DecodingError
        }
        throw AuthError.HttpResponseError
    }

    fun logOutUser() {
        clearSecureStorageData()
        _authState.value = AuthState.Unauthenticated
    }

    fun getRefreshToken(): String? {
        return secureStorageService.read(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
    }

    private fun storeHeaderTokens(headers: okhttp3.Headers?) {
        headers?.let {
            it["X-session-token"]?.let { sessionDetails ->
                secureStorageService.save(
                    TokenType.SESSION_DETAILS.name,
                    secureStorageAccount,
                    sessionDetails
                )
            }
            it["X-auth-header"]?.let { refreshToken ->
                secureStorageService.save(
                    TokenType.REFRESH_TOKEN.name,
                    secureStorageAccount,
                    refreshToken
                )
            }
        }
    }

    private fun storeTokens(refreshToken: String, sessionDetails: String) {
        secureStorageService.save(TokenType.REFRESH_TOKEN.name, secureStorageAccount, refreshToken)
        secureStorageService.save(
            TokenType.SESSION_DETAILS.name,
            secureStorageAccount,
            sessionDetails
        )
    }

    private fun clearSecureStorageData() {
        secureStorageService.delete(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
        secureStorageService.delete(TokenType.SESSION_DETAILS.name, secureStorageAccount)
    }
}
