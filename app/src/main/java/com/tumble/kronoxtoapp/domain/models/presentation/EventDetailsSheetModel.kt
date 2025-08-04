package com.tumble.kronoxtoapp.domain.models.presentation

import com.tumble.kronoxtoapp.domain.models.realm.Event
import java.util.UUID

data class EventDetailsSheetModel(
    val id: UUID = UUID.randomUUID(),
    val event: Event
)