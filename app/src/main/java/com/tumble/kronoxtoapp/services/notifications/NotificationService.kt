package com.tumble.kronoxtoapp.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getString
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.models.findEventsByCategory
import com.tumble.kronoxtoapp.other.extensions.presentation.convertToHoursAndMinutesISOString
import com.tumble.kronoxtoapp.other.extensions.presentation.toLocalDateTime
import com.tumble.kronoxtoapp.services.RealmService
import java.time.ZoneId
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val realmService: RealmService,
): NotificationServiceProtocol {

    override fun cancelNotification(id: String) {
        WorkManager.getInstance(context).cancelUniqueWork(id)
    }

    override fun verifyBookingNotifications() {
        TODO("Not yet implemented")
    }

    override fun isNotificationScheduled(notificationId: String): Boolean {
        return WorkManager.getInstance(context).getWorkInfosForUniqueWork(notificationId).get().isNotEmpty()
    }

    override fun isNotificationScheduledUsingCategory(categoryIdentifier: String): Boolean {
        val events = realmService.getAllSchedules().findEventsByCategory(categoryIdentifier)

        return events.all { isNotificationScheduled(it.eventId) }
    }

    override fun createNotificationUsingCategory(categoryIdentifier: String, userOffset: Int) {
        val events = realmService.getAllSchedules().findEventsByCategory(categoryIdentifier)
        
        for (event in events){
            if (!isNotificationScheduled(event.eventId)){
                createNotificationFromEvent(event, userOffset)
            }
        }
    }

    override fun cancelNotificationsWithCategory(categoryIdentifier: String) {
        val events = realmService.getAllSchedules().findEventsByCategory(categoryIdentifier)

        events.forEach { cancelNotification(it.eventId) }
    }

    override fun cancelNotifications() {
        WorkManager.getInstance(context).cancelAllWorkByTag("event-notification")
        WorkManager.getInstance(context).cancelAllWorkByTag("booking-notification")
    }

    override fun createNotificationFromEvent(event: Event, userOffset: Int) {
        createNotificationChannel("0", context)

        var rooms = ""
        if (!event.locations.isNullOrEmpty()){
            for (i in 0 until  (event.locations?.size ?: 1) - 1){
                rooms += event.locations?.get(i)?.locationId + ", "
            }
            rooms += event.locations?.last()?.locationId
        }
        val from = event.from.convertToHoursAndMinutesISOString()
        val to = event.to.convertToHoursAndMinutesISOString()
        val description = getString(context, R.string.location) + ": "  + rooms
        var longDescription = event.title + "\n" + getString(context, R.string.location) + ": " + rooms
        longDescription += "\n" + getString(context, R.string.timeslot) + ": " + from + " - " + to

        var delay = (event.dateComponents?.timeInMillis ?: 0) - Calendar.getInstance().timeInMillis
        delay = (delay / 1000) - (userOffset * 60)

        scheduleNotification(
            (event.course?.englishName ?: (event.course?.swedishName ?: getString(context, R.string.event_notification))),
            description,
            longDescription,
            "0",
            event.eventId,
            delay
        )
    }

    override fun createNotificationFromBooking(booking: NetworkResponse.KronoxUserBookingElement) {

        if (booking.confirmationOpen == null) {
            Log.d("NotificationManager", "Not scheduling notification. Reason: booking is currently active")
            return
        }

        createNotificationChannel("1", context)
        var description = getString(context, R.string.booking_notification_description_start) + " "
        description += booking.locationId + " " + getString(context, R.string.booking_notification_description_end)

        val time = booking.confirmationOpen.toLocalDateTime()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        val delay = ((time?: 0) - Calendar.getInstance().timeInMillis) / 1000

        if (delay > 0) {
            scheduleNotification(
                getString(context, R.string.confirm_booking),
                description,
                description,
                "1",
                booking.id,
                delay
            )
        }
    }

    override fun areNotificationsAllowed(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    override fun rescheduleEventNotifications(newUserOffset: Int) {
        val events = realmService.getAllSchedules()
            .flatMap { it.days ?: listOf() }
            .flatMap { it.events ?: listOf() }
            .filter { it.dateComponents?.before(Calendar.getInstance()) == false }

        CoroutineScope(Dispatchers.IO).launch {
            val workInfos = WorkManager.getInstance(context)
                .getWorkInfosByTagFlow("event-notification")
                .first()

            val eventIds = workInfos.filter { it.state == WorkInfo.State.ENQUEUED }
                .map { it.tags.last() }

            events.forEach { event ->
                if (event.eventId in eventIds) {
                    createNotificationFromEvent(event, newUserOffset)
                }
            }
        }
    }

    private fun scheduleNotification(title: String, description: String, longDescription: String, channelId: String, notificationId: String, delay: Long) {
        val notificationData = workDataOf(
            "title" to title,
            "description" to description,
            "longDescription" to longDescription,
            "channelId" to channelId,
            "notificationId" to notificationId
        )

        var tag = "booking-notification"
        if (channelId == "0"){
            tag = "event-notification"
        }

        val workRequest = OneTimeWorkRequestBuilder<NotificationScheduleService>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(notificationData)
            .addTag(tag)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            notificationId,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun createNotificationChannel(id: String, context: Context) {
        val channel: NotificationChannel
        if (id == "0") {
            channel = NotificationChannel(
                id,
                getString(context, R.string.event_notification),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(context, R.string.event_notification_description)
            }
        }
        else{
            channel = NotificationChannel(
                id,
                getString(context, R.string.booking_notification),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(context, R.string.booking_notification_description)
            }
        }
        val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}