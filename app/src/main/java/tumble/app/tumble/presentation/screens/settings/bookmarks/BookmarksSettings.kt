package tumble.app.tumble.presentation.screens.settings.bookmarks


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.screens.settings.list.SettingsList
import tumble.app.tumble.presentation.screens.general.Info
import tumble.app.tumble.presentation.screens.navigation.AppBarState

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
            schedules.value.forEach{ schedule ->
                BookmarkSettingsRow(
                    schedule = schedule,
                    onDelete = { offsets ->
                        parentViewModel.deleteBookmark(schedule)
                    },
                    onToggle = {visibility ->
                        parentViewModel.updateBookmarkVisibility(visibility, schedule)
                    }
                )
            }
        }
    } else {
        Info(title = stringResource(R.string.no_bookmarks), image = null)
    }
}
