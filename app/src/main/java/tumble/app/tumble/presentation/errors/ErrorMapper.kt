package tumble.app.tumble.presentation.errors

open class ErrorMapper(private val errorCodeMap: Map<Int, String>) {
    fun mapError(code: Int): String = errorCodeMap[code] ?: "Unknown Error"
}