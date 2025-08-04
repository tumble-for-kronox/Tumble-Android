package com.tumble.kronoxtoapp.data.api

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(val errorMessage: String) : ApiResponse<T>()
}