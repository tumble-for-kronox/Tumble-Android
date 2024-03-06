package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
fun List<Schedule>.filterEventsMatchingToday(): List<Event> {
    val now = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
    val end = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
    val dayRange = now..end

    val eventsForToday = mutableListOf<Event>()
    this.filter { it.toggled }.forEach { event ->
        event.days?.forEach { day ->
            val eventDate = Instant.parse(day.isoString)
            if (dayRange.contains(eventDate)) {
                day.events?.let { eventsForToday.addAll(it) }
            }
        }
    }
    return eventsForToday
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<Schedule>.findNextUpcomingEvent(): Event? {
    val now = LocalDateTime.now()

    // Filter schedules and flatten to get the list of events
    val events: List<Event> = this
        .filter { it.toggled }
        .flatMap { it.days!! }
        .flatMap { it.events!! }

    // Parse dates and make pairs of (event, date)
    val eventDatePairs: List<Pair<Event, LocalDateTime>> = events
        .mapNotNull { event ->
            val date = event.from.toLocalDateTime()
            date?.let { Pair(event, it) }
        }

    val sortedEventDatePairs = eventDatePairs.sortedBy { it.second }

    // Find the first event that is not today and after now
    val nextUpcomingEvent = sortedEventDatePairs.firstOrNull {
        !it.second.toLocalDate().isEqual(now.toLocalDate()) && it.second.isAfter(now)
    }?.first

    return nextUpcomingEvent
}

