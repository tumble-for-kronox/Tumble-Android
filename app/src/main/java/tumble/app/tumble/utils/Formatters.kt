package tumble.app.tumble.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toIsoString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.format(this)
}

val isoDateFormatter: SimpleDateFormat by lazy {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    formatter
}

val isoDateFormatterNoTimeZone: SimpleDateFormat by lazy {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    formatter
}

val isoDateFormatterDate: SimpleDateFormat by lazy {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    formatter
}

val month_date: SimpleDateFormat by lazy {
    val formatter = SimpleDateFormat("MMMM", Locale.getDefault())
    formatter
}