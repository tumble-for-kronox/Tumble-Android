package tumble.app.tumble.datasource.network.extensions

import android.util.Log
import retrofit2.Call
import retrofit2.awaitResponse
import tumble.app.tumble.datasource.network.ApiResponse


suspend fun <T> Call<T>.callToApiResponse(): ApiResponse<T> {
    return try {
        val response = this.awaitResponse()
        if (response.isSuccessful) {
            val responseBody = response.body()
            Log.d("response", response.toString())  // Log the response
            if (responseBody != null) {
                ApiResponse.Success(responseBody)
            } else {
                // If the body is null and empty responses are not allowed, treat it as an error
                ApiResponse.Error("Empty response body", response.code())
            }
        } else {
            // If the response is not successful, return Error with the error message
            ApiResponse.Error(response.errorBody()?.string() ?: "Unknown error", response.code())
        }
    } catch (e: Exception) {
        // In case of exception, return Error with the exception message
        Log.e("error", "in error catch")
        ApiResponse.Error(e.message ?: "Unknown error", null)
    }
}