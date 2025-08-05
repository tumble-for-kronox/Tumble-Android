package com.tumble.kronoxtoapp.services.kronox

import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.KronoxUserBookingElement
import com.tumble.kronoxtoapp.domain.models.network.NewsItems

interface KronoxServiceProtocol {
    suspend fun getNews(): ApiResponse<NewsItems>

    suspend fun getProgramme(endpoint: Endpoint.SearchProgramme): ApiResponse<NetworkResponse.Search>

    suspend fun getSchedule(endpoint: Endpoint.Schedule): ApiResponse<NetworkResponse.Schedule>

    suspend fun getKronoxCompleteUserEvent(endpoint: Endpoint.UserEvents, refreshToken: String?, sessionDetails: String?): ApiResponse<NetworkResponse.KronoxCompleteUserEvent>

    suspend fun getKronoxUserBookings(endpoint: Endpoint.UserBookings, refreshToken: String?, sessionDetails: String?): ApiResponse<List<KronoxUserBookingElement>>

    suspend fun getAllResources(endpoint: Endpoint.AllResources, refreshToken: String?, sessionDetails: String?): ApiResponse<List<NetworkResponse.KronoxResourceElement>>

    suspend fun registerForEvent(endpoint: Endpoint.RegisterEvent, refreshToken: String?): ApiResponse<Void>

    suspend fun bookResource(endpoint: Endpoint.BookResource, refreshToken: String?, resource: NetworkRequest.BookKronoxResource): ApiResponse<KronoxUserBookingElement>

    suspend fun confirmResource(endpoint: Endpoint.ConfirmResource, refreshToken: String?, resource: NetworkRequest.ConfirmKronoxResource): ApiResponse<Void>

    suspend fun unBookResource(endpoint: Endpoint.UnBookResource, refreshToken: String?): ApiResponse<Void>

    suspend fun unRegisterForEvent(endpoint: Endpoint.UnregisterEvent, refreshToken: String?): ApiResponse<Void>

    suspend fun registerForAllEvents(endpoint: Endpoint.RegisterAllEvents, refreshToken: String?): ApiResponse<List<NetworkResponse.Registration>>

}