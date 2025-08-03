package tumble.app.tumble.presentation.screens.settings.preferences.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.screens.settings.buttons.SettingsRadioButton
import tumble.app.tumble.presentation.screens.settings.list.SettingsList
import tumble.app.tumble.presentation.screens.settings.list.SettingsListGroup
import tumble.app.tumble.presentation.screens.navigation.AppBarState
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

        fun fromValue(value: Int): NotificationOffset? {
            return allCases.find { it.value == value }
        }
    }
}

@Composable
fun NotificationOffsetSettings(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.notification_offset)
    val backTitle = stringResource(R.string.accountSettings)

    val currentOffset = viewModel.notificationOffset.collectAsState()

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = pageTitle,
                navigationAction = {
                    BackButton(backTitle) {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

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



