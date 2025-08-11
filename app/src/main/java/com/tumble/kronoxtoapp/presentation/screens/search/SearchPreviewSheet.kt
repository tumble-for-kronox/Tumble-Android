package com.tumble.kronoxtoapp.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.presentation.components.buttons.BookmarkButton
import com.tumble.kronoxtoapp.presentation.viewmodels.SearchPreviewViewModel
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.ButtonState
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.SearchPreviewState
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarkState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchPreviewSheet(
    viewModel: SearchPreviewViewModel = hiltViewModel(),
    scheduleId: String,
    schoolId: String,
    scheduleTitle: String,
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
){
    val state by viewModel.state.collectAsState()

    MakeNavBar(
        setTopNavState = setTopNavState,
        scheduleTitle = scheduleTitle,
        state = state,
        onToggleBookmark = viewModel::toggleBookmark,
        navController = navController,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        when(state) {
            is SearchPreviewState.Loaded -> {
                val data = (state as SearchPreviewState.Loaded)
                SearchPreviewList(
                    schedule = data.schedule,
                    colorsForPreview = data.colorsForPreview
                )
            }
            is SearchPreviewState.Loading -> {
                CustomProgressIndicator()
            }
            is SearchPreviewState.Error -> {
                val errorMessage = (state as SearchPreviewState.Error).errorMessage
                Info(title = errorMessage, image = null)
            }
            is SearchPreviewState.Empty -> {
                Info(title = stringResource(id = R.string.error_empty_schedule), image = null)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSchedule(scheduleId, schoolId, scheduleTitle)
    }
}

@Composable
fun MakeNavBar(
    setTopNavState: (AppBarState) -> Unit,
    scheduleTitle: String,
    state: SearchPreviewState,
    onToggleBookmark: () -> Unit,
    navController: NavController
) {
    LaunchedEffect(state) {
        setTopNavState(
            AppBarState(
                title = scheduleTitle,
                actions = {
                    BookmarkButton(
                        onToggleBookmark = onToggleBookmark,
                        buttonState = getBookmarkButtonState(state)
                    )
                },
                navigationAction = {
                    BackButton("") {
                        navController.popBackStack()
                    }
                }
            )
        )
    }
}

private fun getBookmarkButtonState(state: SearchPreviewState): ButtonState {
    return when (state) {
        is SearchPreviewState.Loaded -> {
            when (state.bookmarkState) {
                is BookmarkState.Loading -> ButtonState.Loading
                is BookmarkState.Bookmarked -> {
                    if (state.bookmarkState.isBookmarked) {
                        ButtonState.Saved
                    } else {
                        ButtonState.NotSaved
                    }
                }
                is BookmarkState.Error -> ButtonState.Disabled
                is BookmarkState.Idle -> ButtonState.Disabled
            }
        }
        else -> ButtonState.Disabled
    }
}