package com.tumble.kronoxtoapp.datasource.network.kronox

import okhttp3.Request
import retrofit2.Retrofit
import com.tumble.kronoxtoapp.data.api.Endpoint
import com.tumble.kronoxtoapp.data.api.url
import com.tumble.kronoxtoapp.datasource.network.ApiResponse
import com.tumble.kronoxtoapp.datasource.network.ApiServiceKronox
import com.tumble.kronoxtoapp.datasource.network.extensions.callToApiResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.KronoxUserBookingElement
import com.tumble.kronoxtoapp.domain.models.network.NewsItems
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KronoxRepository @Inject constructor(private val retrofit: Retrofit): KronoxApiService {
    private val kronoxApiService by lazy {
        retrofit.create(ApiServiceKronox::class.java)
    }

    override suspend fun getNews(): ApiResponse<NewsItems> {
        return kronoxApiService.getNews().callToApiResponse()
    }

     override suspend fun getProgramme(endpoint: Endpoint.SearchProgramme): ApiResponse<NetworkResponse.Search>{
        return kronoxApiService.getProgramme(endpoint.url()).callToApiResponse()
    }

    override suspend fun getSchedule(endpoint: Endpoint.Schedule): ApiResponse<NetworkResponse.Schedule>{
        return kronoxApiService.getSchedule(endpoint.url()).callToApiResponse()
    }

    override suspend fun getKronoxCompleteUserEvent(endpoint: Endpoint.UserEvents, refreshToken: String?, sessionDetails: String?): ApiResponse<NetworkResponse.KronoxCompleteUserEvent>{
        return kronoxApiService.getKronoxCompleteUserEvent(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
    }

    override suspend fun getKronoxUserBookings(endpoint: Endpoint.UserBookings, refreshToken: String?, sessionDetails: String?): ApiResponse<List<KronoxUserBookingElement>>{
        val call = kronoxApiService.getKronoxUserBookings(endpoint.url(), refreshToken, sessionDetails)
        return call.callToApiResponse()
    }

    override suspend fun getAllResources(
        endpoint: Endpoint.AllResources,
        refreshToken: String?,
        sessionDetails: String?
    ): ApiResponse<List<NetworkResponse.KronoxResourceElement>> {
        return kronoxApiService.getAllResources(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
    }

    override suspend fun registerForEvent(endpoint: Endpoint.RegisterEvent, refreshToken: String?):  ApiResponse<Void> {
        return kronoxApiService.registerForEvent(endpoint.url(), refreshToken).callToApiResponse()
    }

    override suspend fun bookResource(endpoint: Endpoint.BookResource, refreshToken: String?, resource: NetworkRequest.BookKronoxResource): ApiResponse<KronoxUserBookingElement> {
        return kronoxApiService.bookResource(endpoint.url(), refreshToken, resource).callToApiResponse()
    }

    override suspend fun confirmResource(
        endpoint: Endpoint.ConfirmResource,
        refreshToken: String?,
        resource: NetworkRequest.ConfirmKronoxResource
    ): ApiResponse<Void> {
        return kronoxApiService.confirmResource(endpoint.url(), refreshToken, resource).callToApiResponse()
    }

    override suspend fun unBookResource(
        endpoint: Endpoint.UnBookResource,
        refreshToken: String?
    ): ApiResponse<Void> {
        return kronoxApiService.unBookResource(endpoint.url(), refreshToken).callToApiResponse()
    }

    override suspend fun unRegisterForEvent(
        endpoint: Endpoint.UnregisterEvent,
        refreshToken: String?
    ): ApiResponse<Void> {
        return kronoxApiService.unRegisterForEvent(endpoint.url(), refreshToken).callToApiResponse()
    }

    override suspend fun registerForAllEvents(
        endpoint: Endpoint.RegisterAllEvents,
        refreshToken: String?
    ): ApiResponse<List<NetworkResponse.Registration>> {
        return kronoxApiService.registerForAllEvents(endpoint.url(), refreshToken).callToApiResponse()
    }

    private fun createRequestWithToken(refreshToken: String?, requestBuilder: Request.Builder): Request {
        refreshToken?.let { token ->
            requestBuilder.header("X-auth-token", token)
        }
        return requestBuilder.build()
    }
}

