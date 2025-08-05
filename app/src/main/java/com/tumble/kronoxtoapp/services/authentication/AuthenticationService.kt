package com.tumble.kronoxtoapp.services.authentication

import com.tumble.kronoxtoapp.domain.models.TumbleUser
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.TokenType
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SecureStorageService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(
    private val authenticationServiceProtocol: AuthenticationServiceProtocol,
    private val secureStorageService: SecureStorageService,
    private val dataStoreService: DataStoreService
) {
    private val secureStorageAccount = "kronoxtoapp-for-kronox"

    suspend fun loginUser(user: NetworkRequest.KronoxUserLogin): TumbleUser {
        val authSchoolId = dataStoreService.authSchoolId.value.toString()
        val response = authenticationServiceProtocol.loginUser(authSchoolId, user)
        if (response.isSuccessful) {
            response.body()?.let {// No need to check header for tokens, first login
                storeTokens(it.refreshToken, "{\"sessionToken\":\"${it.sessionDetails.sessionToken}\",\"sessionLocation\":\"${it.sessionDetails.sessionLocation}\"}")
                return TumbleUser(username = it.username, name = it.name)
            } ?: throw AuthError.DecodingError
        }
        throw AuthError.HttpResponseError
    }

    suspend fun autoLoginUser(): TumbleUser {
        val authSchoolId = dataStoreService.authSchoolId.value.toString()
        val refreshToken = secureStorageService.read(TokenType.REFRESH_TOKEN.name, secureStorageAccount) ?: throw AuthError.TokenError
        val sessionDetails = secureStorageService.read(TokenType.SESSION_DETAILS.name, secureStorageAccount) ?: throw AuthError.TokenError
        val response = authenticationServiceProtocol.autoLoginUser(authSchoolId, refreshToken, sessionDetails)
        if (response.isSuccessful) {
            storeHeaderTokens(response.headers()) // Store potentially new tokens served from backend
            response.body()?.let {
                return TumbleUser(username = it.username, name = it.name)
            } ?: throw AuthError.DecodingError
        }
        throw AuthError.HttpResponseError
    }

    fun logOutUser() {
        clearSecureStorageData()
    }

    fun getRefreshToken(): String? {
        return secureStorageService.read(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
    }

    private fun storeHeaderTokens(headers: okhttp3.Headers?) {
        headers?.let {
            it["X-session-token"]?.let { sessionDetails ->
                secureStorageService.save(TokenType.SESSION_DETAILS.name, secureStorageAccount, sessionDetails)
            }
            it["X-auth-header"]?.let { refreshToken ->
                secureStorageService.save(TokenType.REFRESH_TOKEN.name, secureStorageAccount, refreshToken)
            }
        }
    }

    private fun storeTokens(refreshToken: String, sessionDetails: String) {
        secureStorageService.save(TokenType.REFRESH_TOKEN.name, secureStorageAccount, refreshToken)
        secureStorageService.save(TokenType.SESSION_DETAILS.name, secureStorageAccount, sessionDetails)
    }

    private fun clearSecureStorageData() {
        secureStorageService.delete(TokenType.REFRESH_TOKEN.name, secureStorageAccount)
        secureStorageService.delete(TokenType.SESSION_DETAILS.name, secureStorageAccount)
    }

    sealed class AuthError(message: String) : Exception(message) {
        data object HttpResponseError : AuthError("HTTP Response Error")
        data object TokenError : AuthError("Token Error")
        data object DecodingError : AuthError("Decoding Error")
        data object RequestError : AuthError("Request Error")
    }

}
