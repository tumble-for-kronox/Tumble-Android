package tumble.app.tumble.presentation.views.Settings.Bookmarks


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.general.Info

@Composable
fun BookmarksSettings(
    parentViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val schedules = parentViewModel.bookmarks.collectAsState()

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
        Info(title = stringResource(R.string.no_bookmarks), image = null)
    }
}
