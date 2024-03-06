package tumble.app.tumble.presentation.models

import tumble.app.tumble.domain.models.realm.Event
import java.util.UUID

data class WeekEventCardModel(var event: Event) {
    var id: UUID = UUID.randomUUID()
    var offset: Float = 0F
}