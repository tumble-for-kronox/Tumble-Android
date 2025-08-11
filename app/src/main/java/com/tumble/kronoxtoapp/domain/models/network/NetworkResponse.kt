package com.tumble.kronoxtoapp.domain.models.network

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.AvailabilityValue
import java.util.UUID

typealias NewsItems = List<NetworkResponse.NotificationContent>
typealias availabilities = Map<String, Map<Int, AvailabilityValue>>?

sealed class NetworkResponse {

    @Keep
    @JsonClass(generateAdapter = true)
    data class Schedule(
        val id: String,
        val cachedAt: String,
        val days: List<Day>
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Day(
        val name: String,
        val date: String,
        val isoString: String,
        val weekNumber: Int,
        val events: List<Event>,
        @Transient
        val id: UUID = UUID.randomUUID()
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Event(
        val id: String,
        val title: String,
        val course: Course,
        val from: String,
        val to: String,
        val locations: List<Location>,
        val teachers: List<Teacher>,
        val isSpecial: Boolean,
        val lastModified: String
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Course(
        val id: String,
        val swedishName: String,
        val englishName: String
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Location(
        val id: String,
        val name: String,
        val building: String,
        val floor: String,
        val maxSeats: Int
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Teacher(
        val id: String,
        val firstName: String,
        val lastName: String
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Search(
        val count: Int,
        val items: List<Programme>
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Programme(
        val id: String,
        val title: String,
        val subtitle: String
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxUser(
        val name: String,
        val username: String,
        val refreshToken: String,
        val sessionDetails: SessionDetails
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class SessionDetails(
        val sessionToken: String,
        val sessionLocation: String
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxCompleteUserEvent(
        val upcomingEvents: List<UpcomingKronoxUserEvent>?,
        val registeredEvents: List<AvailableKronoxUserEvent>?,
        val availableEvents: List<AvailableKronoxUserEvent>?,
        val unregisteredEvents: List<AvailableKronoxUserEvent>?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class AvailableKronoxUserEvent(
        @Transient
        val eventId: UUID = UUID.randomUUID(),
        val id: String?,
        val title: String,
        val type: String,
        val eventStart: String,
        val eventEnd: String,
        val lastSignupDate: String,
        val participatorId: String?,
        val supportId: String?,
        val anonymousCode: String,
        val isRegistered: Boolean,
        val supportAvailable: Boolean,
        val requiresChoosingLocation: Boolean
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class UpcomingKronoxUserEvent(
        val title: String,
        val type: String,
        val eventStart: String,
        val eventEnd: String,
        val firstSignupDate: String,
        @Transient
        val id: UUID = UUID.randomUUID()
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxCompleteUserResource(
        val id: String,
        val name: String,
        val timeSlots: JSONNull?,
        val locationIDS: JSONNull?,
        val date: JSONNull?,
        val availabilities: JSONNull?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxResourceElement(
        val id: String?,
        val name: String?,
        val timeSlots: List<TimeSlot>?,
        val date: String?,
        val locationIds: List<String>?,
        val availabilities: Map<String, Map<Int, AvailabilityValue>>?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class AvailabilityValue(
        val availability: AvailabilityEnum?,
        val locationId: String?,
        val resourceType: String?,
        val timeSlotId: String?,
        val bookedBy: String?
    ) : NetworkResponse()

    enum class AvailabilityEnum {
        UNAVAILABLE,
        BOOKED,
        AVAILABLE
    }

    @Keep
    @JsonClass(generateAdapter = true)
    data class TimeSlot(
        val id: Int?,
        val from: String?,
        val to: String?,
        val duration: String?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxUserBookingElement(
        val id: String,
        val resourceId: String,
        val timeSlot: TimeSlot,
        val locationId: String,
        val showConfirmButton: Boolean,
        val showUnbookButton: Boolean,
        val confirmationOpen: String?,
        val confirmationClosed: String?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxUserBookings(
        val bookings: List<KronoxUserBookingElement>
    )

    @Keep
    @JsonClass(generateAdapter = true)
    data class KronoxEventRegistration(
        val successfulRegistrations: List<Registration>?,
        val failedRegistrations: List<Registration>?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class Registration(
        val id: String?,
        val title: String?,
        val type: String?,
        val eventStart: String?,
        val eventEnd: String?,
        val lastSignupDate: String?,
        val participatorId: String?,
        val supportId: String?,
        val anonymousCode: String?,
        val isRegistered: Boolean?,
        val supportAvailable: Boolean?,
        val requiresChoosingLocation: Boolean?
    ) : NetworkResponse()

    @Keep
    @JsonClass(generateAdapter = true)
    data class NotificationContent(
        val topic: String,
        val title: String,
        val body: String,
        val longBody: String?,
        val timestamp: String
    ) : NetworkResponse()

    @JsonClass(generateAdapter = true)
    object JSONNull

}