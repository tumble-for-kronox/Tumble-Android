package tumble.app.tumble.datasource.network

import retrofit2.Response
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Url
import tumble.app.tumble.domain.models.network.NetworkResponse

interface ApiService {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
    )
    @GET
    suspend fun <T : NetworkResponse> get(
        @Url url: String,
        @Header("X-auth-token") token: String?,
    ): Response<T>

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
    )
    @PUT
    suspend fun <T : NetworkResponse> put(
        @Url url: String,
        @Header("X-auth-token") token: String?,
        @Body body: RequestBody
    ): Response<T>
}
