package tumble.app.tumble.datasource.network.kronox

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Url
import tumble.app.tumble.data.api.Endpoint
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NetworkResponse.KronoxUserBookingElement
import tumble.app.tumble.domain.models.network.NewsItems

interface KronoxApiService {
    suspend fun getNews(): ApiResponse<NewsItems>

    suspend fun getProgramme(endpoint: Endpoint.SearchProgramme): ApiResponse<NetworkResponse.Search>

    suspend fun getSchedule(endpoint: Endpoint.Schedule): ApiResponse<NetworkResponse.Schedule>

    suspend fun getKronoxCompleteUserEvent(endpoint: Endpoint.UserEvents, refreshToken: String?, sessionDetails: String?): ApiResponse<NetworkResponse.KronoxCompleteUserEvent>

    suspend fun getKronoxUserBookings(endpoint: Endpoint.UserBookings, refreshToken: String?, sessionDetails: String?): ApiResponse<List<KronoxUserBookingElement>>

    suspend fun getAllResources(endpoint: Endpoint.AllResources, refreshToken: String?, sessionDetails: String?): ApiResponse<List<NetworkResponse.KronoxResourceElement>>

//    @Headers(
//        "Content-Type: application/json; charset=utf-8",
//        "Accept: application/json; charset=utf-8"
//    )
//    @PUT
//    suspend fun <T : NetworkResponse> put(
//        @Url url: String,
//        @Header("X-auth-token") token: String?,
//        @Body body: RequestBody
//    ): Response<T>


//    suspend fun <T : NetworkResponse> get(
//        endpoint: Endpoint,
//        refreshToken: String?,
//        sessionDetails: String?,
//    ): ApiResponse<T>


}