package tumble.app.tumble.datasource.network

import tumble.app.tumble.domain.models.network.NetworkRequest

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(val errorMessage: String, val errorCode: Int? = null) : ApiResponse<T>()
    class Loading<T> : ApiResponse<T>()
}