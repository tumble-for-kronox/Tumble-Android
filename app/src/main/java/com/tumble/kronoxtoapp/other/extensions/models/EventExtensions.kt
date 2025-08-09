package com.tumble.kronoxtoapp.other.extensions.models

import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.utils.DateUtils
import com.tumble.kronoxtoapp.utils.isoDateFormatterNoTimeZone
import java.time.LocalDateTime
import java.time.ZonedDateTime

fun List<Event>.sorted(): List<Event> {
    return this.sortedWith(compareBy { event ->
        isoDateFormatterNoTimeZone.parse(event.from)
    })
}

val Event.fromLocalDateTime: LocalDateTime
    get() = DateUtils.toUserLocalDateTime(this.from)

val Event.toLocalDateTime: LocalDateTime
    get() = DateUtils.toUserLocalDateTime(this.to)

val Event.fromUserZone: ZonedDateTime
    get() = DateUtils.toUserZonedDateTime(this.from)

val Event.toUserZone: ZonedDateTime
    get() = DateUtils.toUserZonedDateTime(this.to)

val Event.displayTime: String
    get() = "${DateUtils.formatTimeOnly(this.from)} - ${DateUtils.formatTimeOnly(this.to)}"

val Event.displayFromTime: String
    get() = DateUtils.formatTimeOnly(this.from)

val Event.displayToTime: String
    get() = DateUtils.formatTimeOnly(this.to)