package com.tumble.kronoxtoapp.domain.models.network

import com.squareup.moshi.JsonClass

sealed class NetworkRequest {

    @JsonClass(generateAdapter = true)
    class Empty

    @JsonClass(generateAdapter = true)
    data class RegisterUserEvent(
        val eventId: String,
        val schoolId: Int,
        val sessionToken: String
    )

    @JsonClass(generateAdapter = true)
    data class UnregisterUserEvent(
        val eventId: String,
        val schoolId: Int,
        val sessionToken: String
    )

    @JsonClass(generateAdapter = true)
    data class RegiserAllUserEvents(
        val schoolId: Int,
        val sessionToken: String
    )

    @JsonClass(generateAdapter = true)
    data class SubmitIssue(
        val title: String,
        val description: String
    )

    @JsonClass(generateAdapter = true)
    data class BookKronoxResource(
        val resourceId: String,
        val date: String,
        val slot: NetworkResponse.AvailabilityValue
    )

    @JsonClass(generateAdapter = true)
    data class ConfirmKronoxResource(
        val resourceId: String,
        val bookingId: String
    )

    @JsonClass(generateAdapter = true)
    data class UnbookKronoxResource(
        val bookingId: String,
        val schoolId: Int
    )

    @JsonClass(generateAdapter = true)
    data class KronoxUserLogin(
        val username: String,
        val password: String
    )
}
