package tumble.app.tumble.data.notifications

import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.realm.Event

interface NotificationApiService {
    fun cancelNotification(id: String)
    fun isNotificationScheduled(eventId: String): Boolean
    fun isNotificationScheduledUsingCategory(categoryIdentifier: String): Boolean
    fun cancelNotificationsWithCategory(categoryIdentifier: String)
    fun cancelNotifications()
    fun createNotificationFromEvent(event: Event, userOffset: Int)
    fun createNotificationFromBooking(booking: NetworkResponse.KronoxUserBookingElement)
    fun areNotificationsAllowed(): Boolean
    fun rescheduleEventNotifications(newUserOffset: Int)
}