package tumble.app.tumble.presentation.views.Settings.Bookmarks


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.presentation.views.navigation.AppBarState

@Composable
fun BookmarksSettings(
    parentViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.bookmark)
    val backTitle = stringResource(R.string.accountSettings)

    val schedules = parentViewModel.bookmarks.collectAsState()

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

    if (schedules.value.isNotEmpty()) {
        SettingsList {
            schedules.value.forEachIndexed { index, schedule ->
                BookmarkSettingsRow(
                    schedule = schedule,
                    index = index,
                    onDelete = { offsets, id ->
                        parentViewModel.deleteBookmark(schedule)
                    }
                )
            }
        }
    } else {
        Info(title = "No bookmarks yet", image = null)
    }
}
