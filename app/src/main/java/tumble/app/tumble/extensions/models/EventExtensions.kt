package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.realm.Event
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun List<Event>.sorted(): List<Event> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return this.sortedWith(compareBy { event ->
        LocalDateTime.parse(event.from, formatter)
    })
}