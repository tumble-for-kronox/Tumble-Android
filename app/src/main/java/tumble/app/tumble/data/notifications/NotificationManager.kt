package tumble.app.tumble.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.notifications.BookingNotification
import tumble.app.tumble.domain.models.notifications.EventNotification
import tumble.app.tumble.domain.models.notifications.LocalNotification
import tumble.app.tumble.domain.models.notifications.NotificationType
import tumble.app.tumble.domain.models.realm.Course
import tumble.app.tumble.domain.models.realm.Event
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreManager: DataStoreManager
): NotificationApiService {
    override fun scheduleNotification(notification: LocalNotification, type: NotificationType, userOffset: Int) {
        if (!areNotificationsAllowed()) {
            return
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent
        when (type) {
            NotificationType.EVENT -> {
                val eventNotification = notification as EventNotification
                intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra("notification_type", type.name)
                    putExtra("notification_id", eventNotification.id)
                    putExtra("notification_category_identifier", eventNotification.categoryIdentifier)
                    putExtra("notification_title", eventNotification.content?.get("title") as String)
                    putExtra("notification_body", (eventNotification.content["course"] as Course).englishName)
                }
            }
            NotificationType.BOOKING -> {
                intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra("notification_type", type.name)
                    putExtra("notification_id", notification.id)
                    putExtra("notification_category_identifier", notification.categoryIdentifier)
                    putExtra("notification_title", "Booking confirmation")
                    putExtra("notification_body", "A booking needs to be confirmed")
                }
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, notification.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, notification.dateComponents.year)
            set(Calendar.MONTH, notification.dateComponents.month - 1)
            set(Calendar.DAY_OF_MONTH, notification.dateComponents.day)
            set(Calendar.HOUR_OF_DAY, notification.dateComponents.hour)
            set(Calendar.MINUTE, notification.dateComponents.minute)
            set(Calendar.SECOND, notification.dateComponents.second)
        }

        if (canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.addNotificationCategory(notification.id, notification.categoryIdentifier)
        }
    }


    override fun cancelNotification(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun isNotificationScheduled(eventId: String): Boolean {
        val allNotifications = dataStoreManager.getScheduledNotifications()
        if (!allNotifications.containsKey(eventId)) {
            return false
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, eventId.hashCode(), intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent != null
    }


    override suspend fun isNotificationScheduledUsingCategory(categoryIdentifier: String): Boolean {
        val allNotifications = dataStoreManager.getScheduledNotifications()

        for ((notificationId, storedCategory) in allNotifications) {
            if (storedCategory == categoryIdentifier) {
                val intent = Intent(context, NotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context, notificationId.hashCode(), intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                if (pendingIntent != null) {
                    return true
                }
            }
        }
        return false
    }


    override fun cancelNotificationsWithCategory(categoryIdentifier: String) {
        TODO("Not yet implemented")
    }

    override fun createNotificationFromEvent(event: Event): EventNotification? {
        event.dateComponents?.let { dateComponents ->
            event.course?.let { course ->
                return EventNotification(
                    id = event.eventId,
                    dateComponents = dateComponents,
                    categoryIdentifier = course.courseId ?: "NO_COURSE",
                    content = event.toMap()
                )
            }
        }
        return null
    }

    override fun createNotificationFromBooking(booking: NetworkResponse.KronoxUserBookingElement): BookingNotification? {
        booking.dateComponentsConfirmation?.let { dateComponents ->
            return BookingNotification(
                id = booking.id,
                dateComponents = dateComponents,
                categoryIdentifier = "booking"
            )
        }
        return null
    }

    override fun areNotificationsAllowed(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }


    override fun rescheduleEventNotifications(previousOffset: Int, userOffset: Int) {
        TODO("Not yet implemented")
    }

    override fun canScheduleExactAlarms(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}