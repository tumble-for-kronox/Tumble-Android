package tumble.app.tumble.domain.models.presentation

import tumble.app.tumble.domain.models.realm.Event
import java.util.UUID

data class EventDetailsSheetModel(
    val id: UUID = UUID.randomUUID(),
    val event: Event
)