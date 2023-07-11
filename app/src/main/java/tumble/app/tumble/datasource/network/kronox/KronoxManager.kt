package tumble.app.tumble.datasource.network.kronox

import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import tumble.app.tumble.datasource.network.ApiService
import tumble.app.tumble.domain.models.network.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KronoxManager @Inject constructor(private val retrofit: Retrofit): ApiService {
    override suspend fun <T : NetworkResponse> get(
        url: String,
        token: String?,
    ): Response<T> {
        val request = createGetRequest(url, token)
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.get(request.url.toString(), token)
    }

    override suspend fun <T : NetworkResponse> put(
        url: String,
        token: String?,
        body: RequestBody
    ): Response<T> {
        val request = createPutRequest(url, body, token)
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.put(request.url.toString(), token, body)
    }

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

