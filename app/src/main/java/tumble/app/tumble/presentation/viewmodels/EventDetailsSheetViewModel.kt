package tumble.app.tumble.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import tumble.app.tumble.data.notifications.NotificationManager
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.realm.RealmManager
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import java.util.Calendar
import javax.inject.Inject

enum class NotificationState{
    SET, LOADING, NOT_SET
}

@HiltViewModel
class EventDetailsSheetViewModel@Inject constructor(
    private val preferenceService: DataStoreManager,
    private val realmManager: RealmManager,
    private val notificationManager: NotificationManager
): ViewModel() {
    lateinit var event: Event// = AppController.shared.eventSheet!!.event
    var color: Color? = null // = event.course?.color?.toColor() ?: Color.Gray
    var isNotificationSetForEvent by mutableStateOf<NotificationState>(NotificationState.LOADING)
    var isNotificationSetForCourse by mutableStateOf<NotificationState>(NotificationState.LOADING)
    val notificationOffset by mutableStateOf<Int>(60)
    var notificationsAllowed by mutableStateOf<Boolean>(false)

    init {
        viewModelScope.launch {
            val allowed = userAllowedNotifications()
            notificationsAllowed = allowed
        }
        //checkNotificationIsSetForEvent()
        //checkNotificationIsSetForCourse()
    }

    fun setEventSheetView(newEvent: Event, newColor: Color?){
        event = newEvent
        color = newColor
        checkNotificationIsSetForEvent()
        checkNotificationIsSetForCourse()
    }

    fun cancelNotificationForEvent(){
        notificationManager.cancelNotification(event.eventId)
        isNotificationSetForEvent = NotificationState.NOT_SET
    }

    fun cancelNotificationForCourse(){
        isNotificationSetForCourse = NotificationState.NOT_SET
        isNotificationSetForEvent = NotificationState.NOT_SET
        event.course?.courseId?.let {
            notificationManager.cancelNotificationsWithCategory(it)
        }
    }

    fun scheduleNotificationForEvent(event: Event){
        isNotificationSetForEvent = NotificationState.LOADING
        notificationManager.createNotificationFromEvent(event, notificationOffset)
        isNotificationSetForEvent = NotificationState.SET
    }

    fun scheduleNotificationForCourse(){
        isNotificationSetForCourse = NotificationState.LOADING
        isNotificationSetForEvent = NotificationState.LOADING

        val events = realmManager.getAllSchedules()
            .flatMap { it.days?: listOf() }
            .flatMap { it.events?: listOf() }
            .filter { it.dateComponents?.before(Calendar.getInstance()) == false }
            .filter { it.course?.courseId == event.course?.courseId }

        viewModelScope.launch {
            try {
                val result = applyNotificationForScheduleEventsInCourse(events)
                if(result){
                    isNotificationSetForCourse = NotificationState.SET
                    isNotificationSetForEvent = NotificationState.SET
                }
            } catch (e: Exception){
                Log.e("e", "Could not set notifications for course: $e")
            }
        }
    }

    private fun applyNotificationForScheduleEventsInCourse(events: List<Event>): Boolean{
        for(event in events){
            scheduleNotificationForEvent(event)
        }
        return true
    }

    private fun userAllowedNotifications(): Boolean{
        return notificationManager.areNotificationsAllowed()
    }

    private fun checkNotificationIsSetForCourse(){
        isNotificationSetForCourse = event.course?.courseId?.let {
            if (notificationManager.isNotificationScheduledUsingCategory(it)){
                NotificationState.SET
            }else{
                NotificationState.NOT_SET
            }
        }?: NotificationState.NOT_SET
    }

    private fun checkNotificationIsSetForEvent(){
        if (notificationManager.isNotificationScheduled(event.eventId)){
            isNotificationSetForEvent = NotificationState.SET
        }else{
            isNotificationSetForEvent = NotificationState.NOT_SET
        }
    }
}