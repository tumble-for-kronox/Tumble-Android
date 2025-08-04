package com.tumble.kronoxtoapp.data.api.extensions

import android.util.Log
import retrofit2.Call
import retrofit2.awaitResponse
import com.tumble.kronoxtoapp.data.api.ApiResponse


suspend fun <T> Call<T>.callToApiResponse(): ApiResponse<T> {
    return try {
        val response = this.awaitResponse()
        if (response.isSuccessful) {
            val responseBody = response.body()
            Log.d("CallExtensions", response.toString())  // Log the response
            if (responseBody != null) {
                ApiResponse.Success(responseBody)
            } else {
                // If the body is null and empty responses are not allowed, treat it as an error
                ApiResponse.Error("Empty response body")
            }
        } else {
            // If the response is not successful, return Error with the error message
            ApiResponse.Error(response.errorBody()?.string() ?: "Unknown error")
        }
    } catch (e: Exception) {
        // In case of exception, return Error with the exception message
        Log.e("error", "in error catch", e)
        ApiResponse.Error(e.message ?: "Unknown error")
    }
}