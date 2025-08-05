package com.tumble.kronoxtoapp.other.extensions.models

import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.utils.isoDateFormatterNoTimeZone

fun List<Event>.sorted(): List<Event> {
    return this.sortedWith(compareBy { event ->
        isoDateFormatterNoTimeZone.parse(event.from)
    })
}