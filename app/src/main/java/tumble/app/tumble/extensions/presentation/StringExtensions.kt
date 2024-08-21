package tumble.app.tumble.extensions.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color
import tumble.app.tumble.utils.isoDateFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.formatDate(): String? {
    //val isoDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.XXX'Z'", Locale.getDefault()) // Adjust this format if your ISO string is in a different format
    val targetFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    val date = isoDateFormatter.parse(this)
    return date?.let { targetFormatter.format(it) }
}

fun String.convertToHoursAndMinutesISOString(): String? {
    //val isoDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) // This format includes the timezone

    val date = isoDateFormatter.parse(this) ?: return null

    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return String.format("%02d:%02d", hour, minute)
}

fun String.toColor(): Color {
    if (!this.matches(Regex("#[a-fA-F0-9]{6}"))) {
        throw IllegalArgumentException("Invalid color format. Must be a 6-digit HEX color.")
    }

    val r = substring(1..2).toInt(16)
    val g = substring(3..4).toInt(16)
    val b = substring(5..6).toInt(16)

    return Color(r, g, b)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: DateTimeParseException) {
        null
    }
}
