package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.presentation.toColor
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.services.notifications.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NotificationState {
    SET, LOADING, NOT_SET
}

sealed class EventDetailsState {
    data object Loading : EventDetailsState()
    data class Loaded(val event: Event, val color: Color) : EventDetailsState()
    data class Error(val message: String) : EventDetailsState()
}

@HiltViewModel
class EventDetailsSheetViewModel @Inject constructor(
    private val realmService: RealmService,
    private val notificationService: NotificationService
) : ViewModel() {

    var eventDetailsState by mutableStateOf<EventDetailsState>(EventDetailsState.Loading)

    var isNotificationSetForEvent by mutableStateOf<NotificationState>(NotificationState.LOADING)
    var isNotificationSetForCourse by mutableStateOf<NotificationState>(NotificationState.LOADING)
    var notificationsAllowed by mutableStateOf<Boolean>(false)
    private val notificationOffset by mutableIntStateOf(60)

    fun getEvent(eventId: String) {
        with(Dispatchers.Main) {
            eventDetailsState = EventDetailsState.Loading
        }

        viewModelScope.launch {
            val realmEvent = realmService.getEvent(eventId)
            if (realmEvent == null) {
                with(Dispatchers.Main) {
                    eventDetailsState =
                        EventDetailsState.Error("Failed to find event with id $eventId in local database")
                }
            }
            realmEvent?.let {
                with(Dispatchers.Main) {
                    val color = realmEvent.course?.color?.toColor() ?: Color.Gray
                    eventDetailsState = EventDetailsState.Loaded(realmEvent, color)
                }
            }
        }
    }

    fun changeCourseColor(colorString: String, courseId: String) {
        val event = (eventDetailsState as EventDetailsState.Loaded).event
        eventDetailsState = EventDetailsState.Loading

        viewModelScope.launch {
            realmService.updateCourseColors(courseId, colorString)
            with(Dispatchers.Main) {
                val newColor = colorString.toColor()
                eventDetailsState = EventDetailsState.Loaded(event, newColor)
            }
        }
    }

    fun cancelNotificationForEvent(event: Event) {
        notificationService.cancelNotification(event.eventId)
        isNotificationSetForEvent = NotificationState.NOT_SET
    }

    fun cancelNotificationForCourse(event: Event) {
        isNotificationSetForCourse = NotificationState.NOT_SET
        isNotificationSetForEvent = NotificationState.NOT_SET
        event.course?.courseId?.let {
            notificationService.cancelNotificationsWithCategory(it)
        }
    }

    fun scheduleNotificationForEvent(event: Event) {
        isNotificationSetForEvent = NotificationState.LOADING
        notificationService.createNotificationFromEvent(event, notificationOffset)
        isNotificationSetForEvent = NotificationState.SET
    }

    fun scheduleNotificationForCourse(event: Event) {
        isNotificationSetForCourse = NotificationState.LOADING
        isNotificationSetForEvent = NotificationState.LOADING

        event.course?.let { course ->
            val events = course.courseId?.let { realmService.getEventsByCourseId(it) }

            viewModelScope.launch {
                try {
                    if (events != null) {
                        val result = applyNotificationForScheduleEventsInCourse(events)
                        if (result) {
                            isNotificationSetForCourse = NotificationState.SET
                            isNotificationSetForEvent = NotificationState.SET
                        }
                    }
                } catch (e: Exception) {
                    Log.e("e", "Could not set notifications for course: $e")
                }
            }
        }
    }

    private fun applyNotificationForScheduleEventsInCourse(events: List<Event>): Boolean {
        for (event in events) {
            scheduleNotificationForEvent(event)
        }
        return true
    }

    private fun userAllowedNotifications(): Boolean {
        return notificationService.areNotificationsAllowed()
    }

    private fun checkNotificationIsSetForCourse(event: Event) {
        isNotificationSetForCourse = event.course?.courseId?.let {
            if (notificationService.isNotificationScheduledUsingCategory(it)) {
                NotificationState.SET
            } else {
                NotificationState.NOT_SET
            }
        } ?: NotificationState.NOT_SET
    }

    private fun checkNotificationIsSetForEvent(event: Event) {
        isNotificationSetForEvent =
            if (notificationService.isNotificationScheduled(event.eventId)) {
                NotificationState.SET
            } else {
                NotificationState.NOT_SET
            }
    }
}