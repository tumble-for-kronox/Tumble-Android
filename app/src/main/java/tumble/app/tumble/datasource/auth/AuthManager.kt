package tumble.app.tumble.datasource.auth

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import tumble.app.tumble.datasource.ApiService
import tumble.app.tumble.domain.models.network.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val retrofit: Retrofit): ApiService {

    override suspend fun <T : NetworkResponse> get(
        url: String,
        token: String?,
        headers: Map<String, String>
    ): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T : NetworkResponse> put(
        url: String,
        token: String?,
        headers: Map<String, String>,
        body: RequestBody
    ): Response<T> {
        TODO("Not yet implemented")
    }

}