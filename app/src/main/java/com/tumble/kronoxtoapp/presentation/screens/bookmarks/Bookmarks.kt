package com.tumble.kronoxtoapp.presentation.screens.bookmarks

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.BookmarksStatus
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.navigation.UriBuilder
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Bookmarks(
    viewModel: BookmarksViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.bookmark)

    val onEventSelection = { event: Event ->
        navController.navigate(UriBuilder.buildBookmarksDetailsUri(eventId = event.eventId).toUri())
    }

    val state by viewModel.state.collectAsState()
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
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
            when (state) {
                is BookmarksState.Loading ->  { CustomProgressIndicator() }
                is BookmarksState.Uninitialized -> {
                    Info(
                        title = stringResource(id = R.string.no_bookmarks),
                        image = null
                    )
                }
                is BookmarksState.AllHidden -> {
                    Info(
                        title = stringResource(id = R.string.bookmarks_hidden),
                        image = null
                    )
                }
                is BookmarksState.Loaded -> {
                    val bookmarkData = (state as BookmarksState.Loaded).bookmarkData
                    BookmarkViewController(
                        bookmarkData = bookmarkData,
                        viewType = viewType,
                        onEventSelection = onEventSelection,
                        setViewType = viewModel::setViewType,
                        updateSelectedDate = viewModel::updateSelectedDate,
                        todaysDate = viewModel.todaysDate,
                        selectedDate = viewModel.selectedDate
                    )
                }

                is BookmarksState.Error -> {
                    Info(
                        title = stringResource(id = R.string.error_something_wrong),
                        image = null
                    )
                }

                is BookmarksState.AllEmpty -> {
                    Info(
                        title = stringResource(id = R.string.schedules_contain_no_events),
                        image = null
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}