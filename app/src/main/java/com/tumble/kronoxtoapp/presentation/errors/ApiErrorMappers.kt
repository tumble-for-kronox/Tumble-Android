package com.tumble.kronoxtoapp.presentation.errors


open class BaseApiErrorMapper constructor(errors: Map<Int, String> = mapOf()) : ErrorMapper(
    mapOf(
        500 to "unknown_api_error"
    ) + errors
) {

}

object NewsApiErrorMapper : BaseApiErrorMapper()