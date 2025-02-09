package tumble.app.tumble.domain.models.notifications

import tumble.app.tumble.domain.models.util.DateComponents

interface LocalNotification {
    val id: String
    val dateComponents: DateComponents
    val categoryIdentifier: String
}
