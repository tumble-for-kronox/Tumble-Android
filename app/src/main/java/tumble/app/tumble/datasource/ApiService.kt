package tumble.app.tumble.datasource

import retrofit2.Response
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Url
import tumble.app.tumble.domain.models.network.NetworkResponse

interface ApiService {

    @GET
    suspend fun <T : NetworkResponse> get(
        @Url url: String,
        @Header("X-auth-token") token: String?,
        @HeaderMap headers: Map<String, String>
    ): Response<T>

    @PUT
    suspend fun <T : NetworkResponse> put(
        @Url url: String,
        @Header("X-auth-token") token: String?,
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Response<T>
}
