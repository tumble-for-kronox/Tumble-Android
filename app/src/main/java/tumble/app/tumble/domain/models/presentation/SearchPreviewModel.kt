package tumble.app.tumble.domain.models.presentation

import java.util.UUID

data class SearchPreviewModel(
    val id: UUID = UUID.randomUUID(),
    val scheduleId: String,
    val schoolId: String,
    val scheduleTitle: String
)