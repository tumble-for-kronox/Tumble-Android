package tumble.app.tumble.presentation.screens.settings.Preferences.AppearanceSettings


import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.Types.AppearanceType
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.screens.settings.Buttons.SettingsRadioButton
import tumble.app.tumble.presentation.screens.settings.List.SettingsList
import tumble.app.tumble.presentation.screens.settings.List.SettingsListGroup
import tumble.app.tumble.presentation.screens.navigation.AppBarState

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