package tumble.app.tumble.presentation.screens.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.BookmarksStatus
import tumble.app.tumble.domain.models.presentation.EventDetailsSheetModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import tumble.app.tumble.presentation.screens.bookmarks.event.EventDetailsSheet
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.general.Info
import tumble.app.tumble.presentation.screens.navigation.AppBarState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Bookmarks(
    viewModel: BookmarksViewModel = hiltViewModel(),
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.bookmark)

    val onEventSelection = { event: Event ->
        viewModel.eventSheet = EventDetailsSheetModel(event = event)
    }

    val bookmarksStatus = viewModel.status
    val viewType = viewModel.defaultViewType.collectAsState()

    LaunchedEffect(key1 = viewModel.eventSheet) {
        setTopNavState(
            AppBarState(
                title = pageTitle
            )
        )
    }

    Column (
        modifier = Modifier
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
    AnimatedVisibility(
        visible = viewModel.eventSheet != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        viewModel.eventSheet?.let {
            EventDetailsSheet(
                event = it.event,
                setTopNavState = setTopNavState,
                onClose = {
                    viewModel.eventSheet = null
                },
                onColorChanged = { hexColor, courseId ->
                    viewModel.changeCourseColor(hexColor, courseId)
                }
            )
        }
    }
}