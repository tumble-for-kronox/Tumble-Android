package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.presentation.viewmodels.EventDetailsSheetViewModel
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.settings.Preferences.Notifications.NotificationOffset
import tumble.app.tumble.presentation.screens.settings.Preferences.Notifications.getOffsetDisplayName
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
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (event.isSpecial) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            color.copy(alpha = 0.08f)
        },
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { openColorPicker() }
                        .semantics {
                            contentDescription = "Change event color"
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notification controls
            if (viewModel.notificationsAllowed) {
                NotificationControls(
                    event = event,
                    viewModel = viewModel
                )
            }
        }
    }

    LaunchedEffect(event) {
        viewModel.setEventSheetView(event, event.course?.color?.toColor())
    }
}

@Composable
private fun NotificationControls(
    event: Event,
    viewModel: EventDetailsSheetViewModel
) {
    val canNotifyEvent = event.from.isAvailableNotificationDate()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Notifications",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Event notifications
        if (viewModel.notificationsAllowed && canNotifyEvent) {
            NotificationToggleRow(
                title = "Notify before this event",
                subtitle = "Get reminded ${getOffsetDisplayName(NotificationOffset.fromValue(viewModel.notificationOffset) ?: NotificationOffset.Thirty)} before",
                state = viewModel.isNotificationSetForEvent,
                onToggle = {
                    when (viewModel.isNotificationSetForEvent) {
                        NotificationState.SET -> viewModel.cancelNotificationForEvent()
                        NotificationState.NOT_SET -> viewModel.scheduleNotificationForEvent(event)
                        else -> {}
                    }
                }
            )
        }

        // Course notifications
        if (viewModel.notificationsAllowed) {
            NotificationToggleRow(
                title = "Notify for all course events",
                subtitle = "Get notified for every event in this course",
                state = viewModel.isNotificationSetForCourse,
                onToggle = {
                    when (viewModel.isNotificationSetForCourse) {
                        NotificationState.SET -> viewModel.cancelNotificationForCourse()
                        NotificationState.NOT_SET -> viewModel.scheduleNotificationForCourse()
                        else -> {}
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    state: NotificationState,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = state != NotificationState.LOADING) { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = when (state) {
                NotificationState.SET -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = when (state) {
                        NotificationState.SET -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (state) {
                        NotificationState.SET -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Toggle indicator
            when (state) {
                NotificationState.SET -> {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications enabled",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                NotificationState.NOT_SET -> {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications disabled",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                NotificationState.LOADING -> {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CustomProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
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