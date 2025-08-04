package com.tumble.kronoxtoapp.datasource.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Url
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.KronoxUserBookingElement
import com.tumble.kronoxtoapp.domain.models.network.NewsItems

interface ApiServiceKronox {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
    )
    @GET("/api/misc/news")
    fun getNews(): Call<NewsItems>

    @GET()
    fun getProgramme(@Url endpoint: String): Call<NetworkResponse.Search>

    @GET()
    fun getSchedule(@Url endpoint: String): Call<NetworkResponse.Schedule>


    @PUT()
    fun registerForEvent(@Url endpoint: String,
                         @Header("X-auth-token") refreshToken: String?): Call<Void>

    @PUT()
    fun registerForAllEvents(@Url endpoint: String,
                         @Header("X-auth-token") refreshToken: String?): Call<List<NetworkResponse.Registration>>

    @PUT()
    fun unRegisterForEvent(@Url endpoint: String,
                           @Header("X-auth-token") refreshToken: String?): Call<Void>

    @PUT()
    fun bookResource(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Body resource: NetworkRequest.BookKronoxResource
    ): Call<KronoxUserBookingElement>

    @PUT()
    fun confirmResource(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Body resource: NetworkRequest.ConfirmKronoxResource
    ): Call<Void>

    @PUT()
    fun unBookResource(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
    ): Call<Void>

    @GET()
    fun getKronoxCompleteUserEvent(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<NetworkResponse.KronoxCompleteUserEvent>

    @GET()
    fun getKronoxUserBookings(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<List<KronoxUserBookingElement>>

    @GET()
    fun getAllResources(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?
    ): Call<List<NetworkResponse.KronoxResourceElement>>

    @GET()
    fun getAllResourceData(
        @Url endpoint: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?,
    ): Call<NetworkResponse.KronoxResourceElement>

    @GET()
    suspend fun <T> get(
        @Url url: String,
        @Header("X-auth-token") refreshToken: String?,
        @Header("X-session-token") sessionDetails: String?,
    ): Call<T>

}
