package com.tumble.kronoxtoapp.domain.models.notifications

import com.tumble.kronoxtoapp.domain.models.util.DateComponents

data class EventNotification(
    override val id: String,
    val dateComponents: DateComponents,
    val categoryIdentifier: String?,
    val content: Map<String, Any>?
) : LocalNotification

