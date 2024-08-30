package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.viewmodels.EventDetailsSheetViewModel
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.utils.isoDateFormatter
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
            .padding()
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
                Text(text = event.course?.englishName.orEmpty(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(bottom = 7.dp)
                )
                Text(text = event.title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Column {
                    if(viewModel.notificationsAllowed){
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            if (event.from.isAvailableNotificationDate()){
                                NotificationPill(
                                    state = viewModel.isNotificationSetForEvent,
                                    title = notificationEventTitle(viewModel),
                                    image = "bell.badge",
                                    onTap = { notificationEventAction(viewModel) }
                                )
                            }
                            NotificationPill(
                                state = viewModel.isNotificationSetForCourse,
                                title = notificationCourseTitle(viewModel),
                                image = "bell.badge",
                                onTap = { notificationCourseAction(viewModel) }
                            )
                        }
                    }
                    ColorPickerPill(openColorPicker = openColorPicker)
                }
            }
        }
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

fun notificationEventAction(viewModel: EventDetailsSheetViewModel){
    when(viewModel.isNotificationSetForEvent) {
        NotificationState.SET -> viewModel.cancelNotificationForEvent()
        NotificationState.NOT_SET -> viewModel.scheduleNotificationForEvent()
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
    val eventDate = isoDateFormatter.parse(this) ?: return false
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR, 3)
    val now = Date()
    val threeHoursFromNow = calendar.time
    return eventDate > now && eventDate > threeHoursFromNow
}
