package com.tumble.kronoxtoapp.other.extensions.presentation

import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color
import com.tumble.kronoxtoapp.utils.isoDateFormatterNoTimeZone
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.formatDate(): String? {
    val targetFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = isoDateFormatterNoTimeZone.parse(this)
    return date?.let { targetFormatter.format(it) }
}

fun String.convertToHoursAndMinutesISOString(): String? {

    val date = isoDateFormatterNoTimeZone.parse(this)?: return null

    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return String.format( Locale.getDefault(),"%02d:%02d", hour, minute)
}

fun String.toColor(): Color {
    val cleanHex = if (this.startsWith("#")) this else "#$this"

    if (!cleanHex.matches(Regex("#[a-fA-F0-9]{6}"))) {
        throw IllegalArgumentException("Invalid color format. Must be a 6-digit HEX color. Received: '$this'")
    }

    val r = cleanHex.substring(1..2).toInt(16)
    val g = cleanHex.substring(3..4).toInt(16)
    val b = cleanHex.substring(5..6).toInt(16)

    return Color(r, g, b)
}

fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun String.isAvailableNotificationDate(): Boolean {
    return try {
        val eventDate = ZonedDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        val now = ZonedDateTime.now()
        val threeHoursFromNow = now.plusHours(3)

        eventDate.isAfter(now) && eventDate.isAfter(threeHoursFromNow)
    } catch (e: Exception) {
        false
    }
}

fun String.isValidRegistrationDate(): Boolean {
    return try {
        // Parse the date from the string
        val date = LocalDateTime.parse(this)
        // Compare it with the current date and time
        date.isAfter(LocalDateTime.now())
    } catch (e: Exception) {
        // Handle any exceptions that may occur during parsing
        false
    }
}