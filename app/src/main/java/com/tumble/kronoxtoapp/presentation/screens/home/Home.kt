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
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.navigation.UriBuilder
import com.tumble.kronoxtoapp.presentation.viewmodels.HomeViewModel
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.home.available.HomeAvailable
import com.tumble.kronoxtoapp.presentation.screens.home.news.News
import com.tumble.kronoxtoapp.presentation.screens.home.news.NewsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.HomeState
import com.tumble.kronoxtoapp.presentation.viewmodels.NewsState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    onComposing: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.home)

    val newsState by viewModel.newsState.collectAsState()
    val homeState by viewModel.homeState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        onComposing(AppBarState(title = pageTitle))
    }

    val onEventSelection = { event: Event ->
        navController.navigate(UriBuilder.buildHomeEventDetailsUri(event.eventId).toUri())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (newsState) {
                is NewsState.Loaded -> {
                    News(
                        news = (newsState as NewsState.Loaded).news,
                        toggleNewsOverlay = viewModel::toggleNewsSheet
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                is NewsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
                is NewsState.Error -> { }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                when (homeState) {
                    is HomeState.BookmarksAvailable -> {
                        HomeAvailable(
                            nextClass = (homeState as HomeState.BookmarksAvailable).nextClass,
                            onEventSelection = onEventSelection
                        )
                    }
                    is HomeState.Loading -> CustomProgressIndicator()
                    is HomeState.NoBookmarks -> HomeNoBookmarks()
                    is HomeState.AllBookmarksHidden -> HomeNotAvailable()
                    is HomeState.Error -> Info(
                        title = stringResource(id = R.string.error_something_wrong),
                        image = null
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.showNewsSheet,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            if (newsState is NewsState.Loaded) {
                NewsSheet(
                    news = (newsState as NewsState.Loaded).news,
                    setTopNavState = onComposing,
                    onClose = {
                        viewModel.toggleNewsSheet(false)
                        onComposing(AppBarState(title = pageTitle))
                    }
                )
            }
        }
    }
}