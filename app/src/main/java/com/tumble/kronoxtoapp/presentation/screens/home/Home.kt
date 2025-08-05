package com.tumble.kronoxtoapp.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.HomeStatus
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.components.buttons.CloseCoverButton
import com.tumble.kronoxtoapp.presentation.navigation.UriBuilder
import com.tumble.kronoxtoapp.presentation.viewmodels.HomeViewModel
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.event.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.home.available.HomeAvailable
import com.tumble.kronoxtoapp.presentation.screens.home.news.News
import com.tumble.kronoxtoapp.presentation.screens.home.news.NewsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.home)

    val newsStatus = viewModel.newsSectionStatus
    val homeStatus = viewModel.status
    val news = viewModel.news

    LaunchedEffect(viewModel.eventSheet) {
        onComposing(AppBarState(title = pageTitle))
    }

    val onEventSelection = { event: Event ->
        navController.navigate(UriBuilder.buildBookmarksDetailsUri(event.eventId))
    }

    val toggleNewsOverlay = { value: Boolean ->
        viewModel.showNewsSheet = value
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (newsStatus == PageState.LOADED) {
            News(news = news, toggleNewsOverlay = toggleNewsOverlay)
        }

        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            when (homeStatus) {
                HomeStatus.AVAILABLE -> HomeAvailable(
                    eventsForToday = viewModel.todaysEventsCards,
                    nextClass = viewModel.nextClass,
                    swipedCards = viewModel.swipedCards,
                    onEventSelection = onEventSelection
                )
                HomeStatus.LOADING -> CustomProgressIndicator()
                HomeStatus.NO_BOOKMARKS -> HomeNoBookmarks()
                HomeStatus.NOT_AVAILABLE -> HomeNotAvailable()
                HomeStatus.ERROR -> Info(
                    title = stringResource(id = R.string.error_something_wrong),
                    image = null
                )
            }
        }

        Spacer(Modifier.weight(1f))
    }

    // News Sheet with built-in navigation
    AnimatedVisibility(
        visible = viewModel.showNewsSheet,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        NewsSheet(
            news = news,
            setTopNavState = onComposing,
            onClose = {
                toggleNewsOverlay(false)
                onComposing(
                    AppBarState(
                        title = pageTitle
                    )
                )
            }
        )
    }
}