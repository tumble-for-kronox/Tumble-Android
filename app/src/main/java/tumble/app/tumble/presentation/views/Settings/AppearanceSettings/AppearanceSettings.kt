package tumble.app.tumble.presentation.views.Settings.AppearanceSettings


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.Types.AppearanceType
import tumble.app.tumble.domain.enums.Types.appearanceTypeToStringResource
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.BackNav
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsRadioButton
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.Settings.List.SettingsListGroup

@Composable
fun AppearanceSettings(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val appearance = viewModel.appearance.collectAsState()

    Scaffold(
        topBar =  {
            BackNav(onClick = { navController.popBackStack() },
                label = stringResource(R.string.settings))
        }
    ) { padding ->

        SettingsList(modifier = Modifier.padding(padding)) {
            SettingsListGroup {
                val appearanceTypes = AppearanceType.values()
                appearanceTypes.forEachIndexed { index, type ->
                    SettingsRadioButton(
                        title = appearanceTypeToStringResource(type),
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
}