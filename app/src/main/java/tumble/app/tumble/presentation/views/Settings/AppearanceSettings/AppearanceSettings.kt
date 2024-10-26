package tumble.app.tumble.presentation.views.Settings.AppearanceSettings


import androidx.compose.material.Divider
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.domain.enums.Types.AppearanceType
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsRadioButton
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.Settings.List.SettingsListGroup

@Composable
fun AppearanceSettings(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val appearance = viewModel.appearance.collectAsState()
    SettingsList {
        SettingsListGroup {
            val appearanceTypes = AppearanceType.values()
            appearanceTypes.forEachIndexed { index, type ->
                SettingsRadioButton(
                    title = stringResource(type.id),
                    isSelected = appearance.value == type,
                    onValueChange = { viewModel.upDateAppearance(type) },
                )
                if (index < appearanceTypes.size - 1) {
                    Divider()
                }
            }
        }
    }
}