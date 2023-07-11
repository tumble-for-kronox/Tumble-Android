package tumble.app.tumble.datasource.kronox

import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import tumble.app.tumble.datasource.ApiService
import tumble.app.tumble.domain.models.network.NetworkResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KronoxManager @Inject constructor(private val retrofit: Retrofit): ApiService {

    override suspend fun <T : NetworkResponse> get(
        url: String,
        token: String?,
        headers: Map<String, String>
    ): Response<T> {
        val request = createGetRequest(url, headers, token)
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.get(request.url.toString(), token, headers)
    }

    override suspend fun <T : NetworkResponse> put(
        url: String,
        token: String?,
        headers: Map<String, String>,
        body: RequestBody
    ): Response<T> {
        val request = createPutRequest(url, headers, body, token)
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.put(request.url.toString(), token, headers, body)
    }

    private fun createGetRequest(
        url: String,
        headers: Map<String, String>,
        refreshToken: String?
    ): Request {
        val requestBuilder = Request.Builder()
            .url(url)
            .get()

        refreshToken?.let { token ->
            requestBuilder.header("X-auth-token", token)
        }

        headers.forEach { (name, value) ->
            requestBuilder.header(name, value)
        }

        return requestBuilder.build()
    }

    private fun createPutRequest(
        url: String,
        headers: Map<String, String>,
        body: RequestBody,
        refreshToken: String?
    ): Request {
        val requestBuilder = Request.Builder()
            .url(url)
            .put(body)

        refreshToken?.let { token ->
            requestBuilder.header("X-auth-token", token)
        }

        headers.forEach { (name, value) ->
            requestBuilder.header(name, value)
        }

        return requestBuilder.build()
    }
}

