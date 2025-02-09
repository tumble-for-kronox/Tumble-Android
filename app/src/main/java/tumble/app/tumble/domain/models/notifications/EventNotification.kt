package tumble.app.tumble.domain.models.notifications

import tumble.app.tumble.domain.models.util.DateComponents

data class EventNotification(
    override val id: String,
    override val dateComponents: DateComponents,
    override val categoryIdentifier: String,
    val content: Map<String, Any>?
) : LocalNotification

