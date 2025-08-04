package com.tumble.kronoxtoapp.presentation.screens.bookmarks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.domain.enums.ViewType
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar.BookmarkCalendarView
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.list.BookmarkListView
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.week.BookmarkWeekView

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun BookmarkViewController(
    viewModel: BookmarksViewModel = hiltViewModel(),
    viewType: State<ViewType>,
    onEventSelection: (Event) -> Unit
){
    val pagerState = rememberPagerState(
        initialPage = viewType.value.ordinal,
        initialPageOffsetFraction = 0f,
        pageCount = { 3 }
    )

    Box{
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 3,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when ( page) {
                ViewType.LIST.ordinal -> {
                    BookmarkListView(
                        onEventSelection = onEventSelection
                    )
                }
                ViewType.WEEK.ordinal -> {
                    BookmarkWeekView(
                        onEventSelection = onEventSelection,
                    )
                }
                ViewType.CALENDAR.ordinal -> {
                    BookmarkCalendarView(onEventSelection = onEventSelection,)
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)) {
            ViewSwitcher(
                pagerState = pagerState,
                viewType = viewType
            )
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage){
        viewModel.setViewType(ViewType.entries[pagerState.currentPage])
    }
}
