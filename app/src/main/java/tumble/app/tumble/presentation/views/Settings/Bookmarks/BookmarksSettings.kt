package tumble.app.tumble.presentation.views.Settings.Bookmarks


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.find
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.BackNav
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.general.Info

@Composable
fun BookmarksSettings(
    parentViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val schedules = parentViewModel.bookmarks.collectAsState()

    Scaffold(
        topBar =  {
            BackNav(onClick = { navController.popBackStack() },
                label = "Settings")
        }
    ) { padding ->
        if (schedules.value.isNotEmpty()) {
            SettingsList(
                modifier = Modifier.padding(padding)
            ) {
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
}
