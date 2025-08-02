package tumble.app.tumble.presentation.screens.settings.Preferences

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.screens.settings.Buttons.SettingsNavigationButton
import tumble.app.tumble.presentation.screens.settings.List.SettingsList
import tumble.app.tumble.presentation.screens.settings.List.SettingsListGroup
import tumble.app.tumble.presentation.screens.navigation.AppBarState
import java.util.Locale

@Composable
fun PreferencesScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.preferences)
    val appearance = viewModel.appearance.collectAsState()

    val currentLocale = Locale.getDefault().displayLanguage
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = pageTitle,
                navigationAction = {
                    BackButton() {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SettingsList {
            SettingsListGroup {
                SettingsNavigationButton(
                    title = stringResource(R.string.app_language),
                    current = currentLocale,
                    leadingIcon = Icons.Default.Language,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = {  startActivity(context, Intent(Settings.ACTION_LOCALE_SETTINGS), null) }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.appearance),
                    current = stringResource(appearance.value.id),
                    leadingIcon = Icons.Default.DarkMode,
                    action = { navController.navigate(Routes.accountSettingsAppearance) }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.notification_offset),
                    leadingIcon = Icons.Default.AccessTime,
                    action = {
                        navController.navigate(Routes.accountSettingsNotifications)
                    }
                )
            }

        }
    }
}