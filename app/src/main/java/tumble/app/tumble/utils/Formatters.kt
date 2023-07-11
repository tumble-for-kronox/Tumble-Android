package tumble.app.tumble.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toIsoString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    return format.format(this)
}

val isoDateFormatter: SimpleDateFormat by lazy {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    formatter
}
