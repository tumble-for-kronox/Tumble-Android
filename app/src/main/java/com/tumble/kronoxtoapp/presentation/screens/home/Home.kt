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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.HomeStatus
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.viewmodels.HomeViewModel
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.event.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.home.available.HomeAvailable
import com.tumble.kronoxtoapp.presentation.screens.home.news.News
import com.tumble.kronoxtoapp.presentation.screens.home.news.NewsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onComposing: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.home)

    val newsStatus = viewModel.newsSectionStatus
    val homeStatus = viewModel.status
    val news = viewModel.news
    val showSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(viewModel.eventSheet) {
        onComposing(AppBarState(title = pageTitle))
    }

    val onEventSelection = { event: Event ->
        viewModel.eventSheet = EventDetailsSheetModel(event = event)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (newsStatus == PageState.LOADED) {
            News(news = news, showOverlay = showSheet)
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

    if (showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            NewsSheet(news = news, sheetState = sheetState, showSheet = showSheet)
        }
    }

    AnimatedVisibility(
        visible = viewModel.eventSheet != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        viewModel.eventSheet?.let {
            EventDetailsSheet(
                event = it.event,
                setTopNavState = onComposing,
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
