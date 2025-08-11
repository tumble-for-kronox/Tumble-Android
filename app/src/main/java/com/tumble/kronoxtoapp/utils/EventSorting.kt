package com.tumble.kronoxtoapp.utils

import com.tumble.kronoxtoapp.domain.models.realm.Event
import java.util.Calendar


fun sortedEventOrder(event1: Event, event2: Event): Int {
    val firstDate = event1.dateComponents?.let { Calendar.getInstance().time }
    val secondDate = event2.dateComponents?.let { Calendar.getInstance().time }

    if (firstDate == null || secondDate == null) {
        return 0
    }

    return if (firstDate.before(secondDate)) 1 else 0
}
