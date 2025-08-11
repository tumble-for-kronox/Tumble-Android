package com.tumble.kronoxtoapp.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object DateUtils {

    private val isoDateFormatterNoTimeZone =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())

    private val commonIsoFormatters = listOf(
        DateTimeFormatter.ISO_INSTANT,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    )

    enum class TimeDisplayMode {
        USER_TIMEZONE,    // Convert to user's local timezone
        LITERAL_TIME      // Show the literal time from the ISO string
    }

    private fun parseIsoStringToInstant(isoString: String): Instant {
        try {
            return Instant.parse(isoString)
        } catch (e: DateTimeParseException) {
            // If no timezone info, try parsing as LocalDateTime and assume system timezone
            for (formatter in commonIsoFormatters) {
                try {
                    val localDateTime = LocalDateTime.parse(isoString, formatter)
                    return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                } catch (e: DateTimeParseException) {
                    continue
                }
            }

            try {
                val date = isoDateFormatterNoTimeZone.parse(isoString)
                return date?.toInstant() ?: Instant.now()
            } catch (e: Exception) {
                return Instant.now()
            }
        }
    }

    // Parse to LocalDateTime without timezone conversion (for literal times)
    private fun parseIsoStringToLiteralDateTime(isoString: String): LocalDateTime {
        return try {
            val instant = Instant.parse(isoString)
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))

            val timeFromOriginal = extractLiteralTimeFromIso(isoString)
            timeFromOriginal ?: zonedDateTime.toLocalDateTime()
        } catch (e: DateTimeParseException) {
            // Try parsing as LocalDateTime directly
            for (formatter in commonIsoFormatters) {
                try {
                    return LocalDateTime.parse(isoString, formatter)
                } catch (e: DateTimeParseException) {
                    continue
                }
            }

            try {
                val date = isoDateFormatterNoTimeZone.parse(isoString)
                return date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
                    ?: LocalDateTime.now()
            } catch (e: Exception) {
                return LocalDateTime.now()
            }
        }
    }

    private fun extractLiteralTimeFromIso(isoString: String): LocalDateTime? {
        return try {
            // Parse the string to extract the literal date/time before timezone conversion
            val dateTimePart =
                isoString.substringBefore('+').substringBefore('Z').substringBefore('-', "")

            for (formatter in commonIsoFormatters) {
                try {
                    return LocalDateTime.parse(dateTimePart, formatter)
                } catch (e: DateTimeParseException) {
                    continue
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    // User timezone conversion methods
    fun toUserLocalDateTime(isoString: String): LocalDateTime {
        val instant = parseIsoStringToInstant(isoString)
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun toUserZonedDateTime(isoString: String): ZonedDateTime {
        val instant = parseIsoStringToInstant(isoString)
        return instant.atZone(ZoneId.systemDefault())
    }

    private fun formatForDisplay(
        isoString: String,
        pattern: String = "MMM dd, yyyy HH:mm",
        mode: TimeDisplayMode = TimeDisplayMode.USER_TIMEZONE
    ): String {
        return try {
            val localDateTime = when (mode) {
                TimeDisplayMode.USER_TIMEZONE -> toUserLocalDateTime(isoString)
                TimeDisplayMode.LITERAL_TIME -> parseIsoStringToLiteralDateTime(isoString)
            }
            val formatter = DateTimeFormatter.ofPattern(pattern)
            localDateTime.format(formatter)
        } catch (e: Exception) {
            isoString
        }
    }

    fun formatTimeOnly(
        isoString: String,
        mode: TimeDisplayMode = TimeDisplayMode.USER_TIMEZONE
    ): String {
        return formatForDisplay(isoString, "HH:mm", mode)
    }

    fun formatDateOnly(
        isoString: String,
        mode: TimeDisplayMode = TimeDisplayMode.USER_TIMEZONE
    ): String {
        return formatForDisplay(isoString, "MMM dd, yyyy", mode)
    }


    // For schedule events (convert to user timezone)
    fun formatScheduleTime(isoString: String): String {
        return formatTimeOnly(isoString, TimeDisplayMode.USER_TIMEZONE)
    }

    fun formatScheduleDate(isoString: String): String {
        return formatDateOnly(isoString, TimeDisplayMode.USER_TIMEZONE)
    }

    // For institutional/resource times (show literal time)
    fun formatInstitutionalTime(isoString: String): String {
        Log.d("DateUtils", "Formatting string $isoString")
        return formatTimeOnly(isoString, TimeDisplayMode.LITERAL_TIME)
    }

    fun formatInstitutionalDate(isoString: String): String {
        return formatDateOnly(isoString, TimeDisplayMode.LITERAL_TIME)
    }
}