package com.tumble.kronoxtoapp.domain.models.notifications

import com.tumble.kronoxtoapp.domain.models.util.DateComponents

data class BookingNotification(
    override val id: String,
    val dateComponents: DateComponents
) : LocalNotification