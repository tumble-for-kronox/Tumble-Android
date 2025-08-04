package com.tumble.kronoxtoapp.presentation.errors

open class ErrorMapper(private val errorCodeMap: Map<Int, String>) {
    fun mapError(code: Int): String = errorCodeMap[code] ?: "Unknown Error"
}