package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.presentation.viewmodels.EventDetailsSheetViewModel
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.utils.isoDateFormatterNoTimeZone
import java.util.Date

@Composable
fun EventDetailsCard(
    viewModel: EventDetailsSheetViewModel = hiltViewModel(),
    openColorPicker: () -> Unit,
    event: Event,
    color: Color
){
    Column(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .background(
                if (event.isSpecial) Color.Red.copy(alpha = 0.2f) else color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = event.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 5.dp, bottom = 8.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.5.dp)) {
                    if(viewModel.notificationsAllowed){
                        if (event.from.isAvailableNotificationDate()){
                                NotificationPill(
                                    state = viewModel.isNotificationSetForEvent,
                                    title = notificationEventTitle(viewModel),
                                    onTap = { notificationEventAction(viewModel, event) }
                                )
                            }
                            NotificationPill(
                                state = viewModel.isNotificationSetForCourse,
                                title = notificationCourseTitle(viewModel),
                                onTap = { notificationCourseAction(viewModel) }
                            )
                    }
                    ColorPickerPill(openColorPicker = openColorPicker)
                }
            }
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.setEventSheetView(event, event.course?.color?.toColor())
    }
}

@Composable
fun notificationEventTitle(viewModel: EventDetailsSheetViewModel): String{
    return when (viewModel.isNotificationSetForEvent){
        NotificationState.SET -> "Remove"
        NotificationState.NOT_SET -> "Event"
        else -> ""
    }
}

@Composable
fun notificationCourseTitle(viewModel: EventDetailsSheetViewModel): String{
    return when (viewModel.isNotificationSetForCourse){
        NotificationState.SET -> "Remove"
        NotificationState.NOT_SET -> "Course"
        else -> ""
    }
}

fun notificationEventAction(viewModel: EventDetailsSheetViewModel, event: Event){
    when(viewModel.isNotificationSetForEvent) {
        NotificationState.SET -> viewModel.cancelNotificationForEvent()
        NotificationState.NOT_SET -> viewModel.scheduleNotificationForEvent(event = event)
        else -> {}
    }
}

fun notificationCourseAction(viewModel: EventDetailsSheetViewModel){
    when(viewModel.isNotificationSetForCourse) {
        NotificationState.SET -> viewModel.cancelNotificationForCourse()
        NotificationState.NOT_SET -> viewModel.scheduleNotificationForCourse()
        else -> {}
    }
}

private fun String.isAvailableNotificationDate(): Boolean{
    val eventDate = isoDateFormatterNoTimeZone.parse(this) ?: return false
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR, 3)
    val now = Date()
    val threeHoursFromNow = calendar.time
    return eventDate > now && eventDate > threeHoursFromNow
}

