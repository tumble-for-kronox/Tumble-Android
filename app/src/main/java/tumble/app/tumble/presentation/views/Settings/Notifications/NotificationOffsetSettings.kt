package tumble.app.tumble.presentation.views.Settings.Notifications

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsRadioButton
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.Settings.List.SettingsListGroup
import java.util.UUID

enum class NotificationOffset(val value: Int) {
    Fifteen(15),
    Thirty(30),
    Hour(60),
    ThreeHours(180);

    val id: UUID
        get() = UUID.randomUUID()

    companion object {
        val allCases = listOf(Fifteen, Thirty, Hour, ThreeHours)
    }
}

@Composable
fun NotificationOffsetSettings(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val currentOffset = viewModel.notificationOffset.collectAsState()
    SettingsList {
        SettingsListGroup {
            NotificationOffset.allCases.forEach { type ->
                SettingsRadioButton(
                    title = getOffsetDisplayName(type),
                    isSelected = currentOffset.value.value == type.value,
                    onValueChange = {
                        val previousOffset = currentOffset
                        viewModel.rescheduleNotifications(
                            previousOffset.value.value,
                            type
                        )
                    }
                )
                if (type != NotificationOffset.allCases.last()) {
                    Divider(Modifier.padding(horizontal = 10.dp))
                }
            }
        }
    }
}

@Composable
fun getOffsetDisplayName(offset: NotificationOffset): String {
    val minutes = offset.value
    return if (minutes < 60) {
        "$minutes ${"minutes"}"
    } else {
        val hours = minutes / 60
        if (hours == 1) {
            "$hours ${"hour"}"
        } else {
            "$hours ${"hours"}"
        }
    }
}



