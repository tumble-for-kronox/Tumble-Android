package com.tumble.kronoxtoapp.presentation.models

import com.tumble.kronoxtoapp.domain.models.realm.Event
import java.util.UUID

data class WeekEventCardModel(var event: Event) {
    var id: UUID = UUID.randomUUID()
    var offset: Float = 0F
}