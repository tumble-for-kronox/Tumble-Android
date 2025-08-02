package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    event: Event,
    color: Color,
    openColorPicker: () -> Unit,
    viewModel: EventDetailsSheetViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (event.isSpecial) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            color.copy(alpha = 0.12f)
        },
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (viewModel.notificationsAllowed &&
                (event.from.isAvailableNotificationDate() || viewModel.isNotificationSetForCourse != NotificationState.NOT_SET)) {
                ActionPillsRow(
                    event = event,
                    viewModel = viewModel,
                    openColorPicker = openColorPicker
                )
            } else {
                ActionPillsRow(
                    event = event,
                    viewModel = viewModel,
                    openColorPicker = openColorPicker,
                    showNotifications = false
                )
            }
        }
    }

    LaunchedEffect(event) {
        viewModel.setEventSheetView(event, event.course?.color?.toColor())
    }
}

@Composable
private fun ActionPillsRow(
    event: Event,
    viewModel: EventDetailsSheetViewModel,
    openColorPicker: () -> Unit,
    showNotifications: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (event.from.isAvailableNotificationDate()) {
            NotificationPill(
                state = viewModel.isNotificationSetForEvent,
                title = getNotificationEventTitle(viewModel.isNotificationSetForEvent),
                onTap = { handleNotificationEventAction(viewModel, event) }
            )
        }
        NotificationPill(
            state = viewModel.isNotificationSetForCourse,
            title = getNotificationCourseTitle(viewModel.isNotificationSetForCourse),
            onTap = { handleNotificationCourseAction(viewModel) }
        )
        ColorPickerPill(onClick = openColorPicker)
    }
}

// Helper functions
private fun getNotificationEventTitle(state: NotificationState): String = when (state) {
    NotificationState.SET -> "Remove"
    NotificationState.NOT_SET -> "Event"
    else -> ""
}

private fun getNotificationCourseTitle(state: NotificationState): String = when (state) {
    NotificationState.SET -> "Remove"
    NotificationState.NOT_SET -> "Course"
    else -> ""
}

private fun handleNotificationEventAction(viewModel: EventDetailsSheetViewModel, event: Event) {
    when (viewModel.isNotificationSetForEvent) {
        NotificationState.SET -> viewModel.cancelNotificationForEvent()
        NotificationState.NOT_SET -> viewModel.scheduleNotificationForEvent(event = event)
        else -> {}
    }
}

private fun handleNotificationCourseAction(viewModel: EventDetailsSheetViewModel) {
    when (viewModel.isNotificationSetForCourse) {
        NotificationState.SET -> viewModel.cancelNotificationForCourse()
        NotificationState.NOT_SET -> viewModel.scheduleNotificationForCourse()
        else -> {}
    }
}

private fun String.isAvailableNotificationDate(): Boolean {
    val eventDate = isoDateFormatterNoTimeZone.parse(this) ?: return false
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR, 3)
    val now = Date()
    val threeHoursFromNow = calendar.time
    return eventDate > now && eventDate > threeHoursFromNow
}
