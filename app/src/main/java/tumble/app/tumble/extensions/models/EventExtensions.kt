package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.utils.isoDateFormatter
import tumble.app.tumble.utils.isoDateFormatterNoTimeZone
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun List<Event>.sorted(): List<Event> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return this.sortedWith(compareBy { event ->
        isoDateFormatterNoTimeZone.parse(event.from)
        //LocalDateTime.parse(event.from, formatter)
    })
}