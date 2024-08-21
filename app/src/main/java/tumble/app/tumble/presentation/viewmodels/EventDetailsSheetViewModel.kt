package tumble.app.tumble.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString
import tumble.app.tumble.datasource.preferences.DataStoreManager
import tumble.app.tumble.datasource.realm.RealmManager
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.observables.AppController
import java.util.Date
import javax.inject.Inject

enum class NotificationState{
    SET, LOADING, NOT_SET
}

class EventDetailsSheetViewModel@Inject constructor(
    private val preferenceService: DataStoreManager,
    private val realmManager: RealmManager,
): ViewModel() {
    var event = AppController.shared.eventSheet!!.event
    var color = event.course?.color?.toColor() ?: Color.Gray
    var isNotificationSetForEvent by mutableStateOf<NotificationState>(NotificationState.LOADING)
    var isNotificationSetForCourse by mutableStateOf<NotificationState>(NotificationState.LOADING)
    val notificationOffset by mutableStateOf<Int>(60)
    var notificationsAllowed by mutableStateOf<Boolean>(false)
    private val oldColor: Color = event.course?.color?.toColor() ?: Color.White

    init {
        viewModelScope.launch {
            val allowed = userAllowedNotifications()
            notificationsAllowed = allowed
            checkNotificationIsSetForEvent()
            checkNotificationIsSetForCourse()
        }
    }

    fun setEventSheetView(newEvent: Event, newColor: Color){
        event = newEvent
        color = newColor
    }

    fun cancelNotificationForEvent(){
        isNotificationSetForEvent = NotificationState.NOT_SET
    }

    fun cancelNotificationForCourse(){
        isNotificationSetForCourse = NotificationState.NOT_SET
        isNotificationSetForEvent = NotificationState.NOT_SET
        event.course?.courseId?.let { courseId ->
            viewModelScope.launch {
            }
        }
    }

    fun scheduleNotificationForEvent(){
        isNotificationSetForEvent = NotificationState.LOADING
        // offset
        val notification = event.dateComponents?.let {
        }
    }

    suspend fun updateCourseColor(){
        if(oldColor == color) return
        val colorHex = color.toArgb().toHexString() ?:return
        val courseId = event.course?.courseId ?: return
        realmManager.updateCourseColors(courseId, colorHex)
    }

    fun scheduleNotificationForCourse(){
        isNotificationSetForCourse = NotificationState.LOADING
        isNotificationSetForEvent = NotificationState.LOADING

        val events = realmManager.getAllSchedules()
            .flatMap { it.days?: listOf() }
            .flatMap { it.events?: listOf() }
            .filter { it.dateComponents?.before(Date()) == false }
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

    private suspend fun userAllowedNotifications(): Boolean{
        return false
    }

    private suspend fun applyNotificationForScheduleEventsInCourse(events: List<Event>): Boolean{
        return false
    }

    private fun checkNotificationIsSetForCourse(){}

    private fun checkNotificationIsSetForEvent(){}
}