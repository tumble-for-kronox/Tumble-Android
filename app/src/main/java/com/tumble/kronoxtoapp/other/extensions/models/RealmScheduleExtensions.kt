package com.tumble.kronoxtoapp.other.extensions.models

import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.toRealmList
import com.tumble.kronoxtoapp.domain.models.realm.Day
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.other.extensions.presentation.toLocalDateTime
import com.tumble.kronoxtoapp.utils.preprocessDateString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Calendar

fun List<Schedule>.filterEventsMatchingToday(): List<Event> {
    val now = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
    val end = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
    val dayRange = now..end

    val eventsForToday = mutableListOf<Event>()
    this.filter { it.toggled }.forEach { event ->
        event.days?.forEach { day ->
            val preProcessedDate = day.isoString?.let { preprocessDateString(it) }
            // Use OffsetDateTime to parse the date with offset and then convert to Instant
            val eventDate = OffsetDateTime.parse(preProcessedDate).toInstant()
            if (dayRange.contains(eventDate)) {
                day.events?.let { eventsForToday.addAll(it) }
            }
        }
    }
    return eventsForToday
}

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

fun List<Schedule>.flattenAndMerge(): List<Day> {
    val days: MutableList<Day> = mutableListOf()
    this.forEach { days += it.days?.toList() ?: emptyList() }
    val dayDictionary: MutableMap<String, Day> = mutableMapOf()

    for (day in days){
        val existingDay = dayDictionary[day.isoString]
        if (existingDay != null){
            day.events?.let { existingDay.events?.addAll(it) }
            day.isoString?.let { dayDictionary.put(it, existingDay) }
        }else{
            val newDay = Day()
            newDay.name = day.name
            newDay.date = day.date
            newDay.isoString = day.isoString
            newDay.weekNumber = day.weekNumber
            newDay.events = day.events?.copyFromRealm()?.toRealmList()
            day.isoString?.let { dayDictionary.put(it, newDay) }
        }
    }
    return dayDictionary.values.toList()
}

fun Schedule.isMissingEvents(): Boolean{
    if (this.days == null){
        return true
    }
    for (day in this.days!!){
        for (event in day.events!!){
            if (event.title.isNotEmpty()){
                return false
            }
        }
   }
    return true
}

fun List<Schedule>.findEventsByCategory(categoryIdentifier: String): List<Event>{
    return this.asSequence().flatMap { it.days?: listOf() }
        .flatMap { it.events?: listOf() }
        .filter { it.dateComponents?.before(Calendar.getInstance()) == false }
        .filter { it.course?.courseId == categoryIdentifier }
        .toList()
}