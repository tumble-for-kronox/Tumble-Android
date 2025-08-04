package com.tumble.kronoxtoapp.presentation.screens.settings.preferences.appearance


import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.Types.AppearanceType
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.viewmodels.SettingsViewModel
import com.tumble.kronoxtoapp.presentation.screens.settings.buttons.SettingsRadioButton
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsList
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsListGroup
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

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
            val appearanceTypes = AppearanceType.values()
            appearanceTypes.forEach { type ->
                SettingsRadioButton(
                    title = stringResource(type.id),
                    isSelected = appearance.value == type,
                    onValueChange = { viewModel.updateAppearance(type) },
                )
            }
        }
    }
}