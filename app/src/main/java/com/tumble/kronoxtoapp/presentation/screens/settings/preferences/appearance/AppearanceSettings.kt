package com.tumble.kronoxtoapp.presentation.screens.settings.preferences.appearance


import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.types.AppearanceType
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.screens.settings.buttons.SettingsRadioButton
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsList
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsListGroup
import com.tumble.kronoxtoapp.presentation.viewmodels.SettingsViewModel

@Composable
fun AppearanceSettings(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {

    val pageTitle = stringResource(R.string.appearance)
    val backTitle = stringResource(R.string.accountSettings)

    val appearance = viewModel.appearance.collectAsState()

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
            SettingsRadioButton(
                title = stringResource(AppearanceType.AUTOMATIC.id),
                isSelected = appearance.value == AppearanceType.AUTOMATIC,
                onValueChange = { viewModel.updateAppearance(AppearanceType.AUTOMATIC) },
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            SettingsRadioButton(
                title = stringResource(AppearanceType.LIGHT.id),
                isSelected = appearance.value == AppearanceType.LIGHT,
                onValueChange = { viewModel.updateAppearance(AppearanceType.LIGHT) },
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            SettingsRadioButton(
                title = stringResource(AppearanceType.DARK.id),
                isSelected = appearance.value == AppearanceType.DARK,
                onValueChange = { viewModel.updateAppearance(AppearanceType.DARK) },
            )
        }
    }
}