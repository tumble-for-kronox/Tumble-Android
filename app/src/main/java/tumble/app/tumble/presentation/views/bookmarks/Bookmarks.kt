package tumble.app.tumble.presentation.views.bookmarks

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.BookmarksStatus
import tumble.app.tumble.domain.models.presentation.EventDetailsSheetModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.navigation.UriBuilder
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import tumble.app.tumble.presentation.viewmodels.ParentViewModel
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator
import tumble.app.tumble.presentation.views.general.Info

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Bookmarks(
    viewModel: BookmarksViewModel = hiltViewModel(),
    parentViewModel: ParentViewModel = hiltViewModel(),
    navController: NavController,
) {

    val onEventSelection = { event: Event ->
        AppController.shared.eventSheet = EventDetailsSheetModel(event = event)
        navController.navigate( UriBuilder.buildBookmarksDetailsUri(event.eventId).toUri() )
    }

    val bookmarksStatus = viewModel.status
    val viewType = viewModel.defaultViewType.collectAsState()

    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.bookmark)) },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 12.dp
                )
            }
        },
        backgroundColor = Color.Transparent
    ){padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxSize()
        ) {
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
                when(bookmarksStatus){
                    BookmarksStatus.LOADING -> CustomProgressIndicator()
                    BookmarksStatus.LOADED -> { BookmarkViewController(viewType = viewType, onEventSelection = onEventSelection) }
                    BookmarksStatus.UNINITIALIZED -> Info(title = stringResource(id = R.string.no_bookmarks), image = null)
                    BookmarksStatus.HIDDEN_ALL -> Info(title = stringResource(id = R.string.bookmarks_hidden), image = null)
                    BookmarksStatus.ERROR -> Info(title = stringResource(id = R.string.error_something_wrong), image = null)
                    BookmarksStatus.EMPTY -> Info(title = stringResource(id = R.string.schedules_contain_no_events), image = null)
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}