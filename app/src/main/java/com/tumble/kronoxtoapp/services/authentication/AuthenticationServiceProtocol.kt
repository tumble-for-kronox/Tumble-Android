package com.tumble.kronoxtoapp.services.authentication

import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationServiceProtocol {
    @POST("/api/users/login")
    suspend fun loginUser(
        @Query("schoolId") schoolId: String,
        @Body user: NetworkRequest.KronoxUserLogin): Response<NetworkResponse.KronoxUser>

    @GET("/api/users")
    suspend fun autoLoginUser(
        @Query("schoolId") schoolId: String,
        @Header("X-auth-token") refreshToken: String,
        @Header("X-session-token") sessionDetails: String
    ): Response<NetworkResponse.KronoxUser>
}

