package com.tumble.kronoxtoapp.presentation.screens.settings.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.types.AppearanceType
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.viewmodels.SettingsViewModel
import com.tumble.kronoxtoapp.presentation.screens.settings.buttons.SettingsNavigationButton
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsList
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsListGroup
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@Composable
fun PreferencesScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.preferences)
    val appearance = viewModel.appearance.collectAsState()

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
            SettingsListGroup (title = "General") {
                SettingsNavigationButton(
                    title = stringResource(R.string.appearance),
                    current = stringResource(appearance.value.id),
                    trailingIcon = Icons.Default.ChevronRight,
                    leadingIcon = if (appearance.value == AppearanceType.DARK) {
                        Icons.Default.DarkMode
                    } else {
                        Icons.Default.LightMode
                    },
                    action = { navController.navigate(Routes.accountSettingsAppearance) }
                )
//                SettingsNavigationButton(
//                    title = stringResource(R.string.notification_offset),
//                    leadingIcon = Icons.Default.AccessTime,
//                    action = {
//                        navController.navigate(Routes.accountSettingsNotifications)
//                    }
//                )
            }

        }
    }
}