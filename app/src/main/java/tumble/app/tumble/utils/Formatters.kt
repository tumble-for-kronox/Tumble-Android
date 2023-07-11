package tumble.app.tumble.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toIsoString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    return format.format(this)
}
