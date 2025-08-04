package com.tumble.kronoxtoapp.extensions.models

import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.utils.isoDateFormatterNoTimeZone
import java.time.format.DateTimeFormatter

fun List<Event>.sorted(): List<Event> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return this.sortedWith(compareBy { event ->
        isoDateFormatterNoTimeZone.parse(event.from)
        //LocalDateTime.parse(event.from, formatter)
    })
}