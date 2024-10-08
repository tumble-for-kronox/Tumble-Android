package tumble.app.tumble.datasource.network.kronox

import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.data.api.url
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.ApiServiceKronox
import tumble.app.tumble.datasource.network.extensions.callToApiResponse
import tumble.app.tumble.domain.models.network.NetworkRequest
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NetworkResponse.KronoxUserBookingElement
import tumble.app.tumble.domain.models.network.NewsItems
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
        return kronoxApiService.getKronoxUserBookings(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
    }

    override suspend fun getAllResources(endpoint: Endpoint.AllResources, refreshToken: String?, sessionDetails: String?): ApiResponse<List<NetworkResponse.KronoxResourceElement>>{
        return kronoxApiService.getAllResources(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
    }

//    override suspend fun getAllResourcesTypes(endpoint: Endpoint.AllResourcesTest, refreshToken: String?, sessionDetails: String?): ApiResponse<List<NetworkResponse.KronoxResourceElement>>{
//        return kronoxApiService.getAllResourcesTypes(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
//    }

    override suspend fun getAllResourceData(endpoint: Endpoint.AllResourceData, refreshToken: String?, sessionDetails: String?): ApiResponse<NetworkResponse.KronoxResourceElement>{
        return kronoxApiService.getAllResourceData(endpoint.url(), refreshToken, sessionDetails).callToApiResponse()
    }

    override suspend fun registerForEvent(endpoint: Endpoint.RegisterEvent, refreshToken: String?): Response<NetworkResponse.Empty> {
        return kronoxApiService.registerForEvent(endpoint.url(), refreshToken)
    }

    override suspend fun bookResource(endpoint: Endpoint.BookResource, refreshToken: String?, resource: NetworkRequest.BookKronoxResource): Response<NetworkResponse.Empty> {
        return kronoxApiService.bookResource(endpoint.url(), refreshToken, resource)
    }

    override suspend fun confirmResource(
        endpoint: Endpoint.ConfirmResource,
        refreshToken: String?,
        resource: NetworkRequest.ConfirmKronoxResource
    ): Response<NetworkResponse.Empty> {
        return kronoxApiService.confirmResource(endpoint.url(), refreshToken, resource)
    }

    override suspend fun unBookResource(
        endpoint: Endpoint.UnBookResource,
        refreshToken: String?
    ): Response<NetworkResponse.Empty> {
        return kronoxApiService.unBookResource(endpoint.url(), refreshToken)
    }

//    override suspend fun <T: NetworkResponse> get(
//        endpoint: Endpoint,
//        refreshToken: String?,
//        sessionDetails: String?
//    ): ApiResponse<T> {
//        val url = endpoint.url()
//        return kronoxApiService.get<T>(url, refreshToken, sessionDetails).callToApiResponse()
//    }

//    suspend fun <T :NetworkResponse> put(
//        url: String,
//        token: String?,
//        body: RequestBody
//    ): Response<NetworkResponse> {
//        val request = createPutRequest(url, body, token)
//        val apiService = retrofit.create(ApiServiceKronox::class.java)
//        return apiService.put(request.url.toString(), token, body)
//    }

    private fun createGetRequest(
        url: String,
        refreshToken: String?
    ): Request {
        return createRequestWithToken(
            refreshToken,
            Request.Builder()
            .url(url)
            .get()
        )
    }

    private fun createPutRequest(
        url: String,
        body: RequestBody,
        refreshToken: String?
    ): Request {
        return createRequestWithToken(
            refreshToken,
            Request.Builder()
                .url(url)
                .put(body)
        )
    }

    private fun createRequestWithToken(refreshToken: String?, requestBuilder: Request.Builder): Request {
        refreshToken?.let { token ->
            requestBuilder.header("X-auth-token", token)
        }
        return requestBuilder.build()
    }
}

