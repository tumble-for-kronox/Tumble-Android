package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import android.icu.util.Calendar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.utils.month_date
import java.time.LocalDate
import kotlin.math.abs

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun BookmarkCalendarView(
    onEventSelection: (Event) -> Unit,
    calendarEventsByDate: Map<LocalDate, List<Event>>,
    updateSelectedDate: (LocalDate) -> Unit,
    todaysDate: LocalDate,
    selectedDate: LocalDate
) {
    val monthTitlePagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 6 }
    )
    val datesPagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 6 }
    )

    val scroll = rememberLazyListState()
    LazyColumn(state = scroll, modifier = Modifier.padding(horizontal = 15.dp)) {
        item {
            Spacer(Modifier.height(15.dp))
            VerticalPager(
                state = monthTitlePagerState,
                beyondViewportPageCount = 3,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) { page ->
                MonthName(page = page)
            }

            DaysOfTheWeek()

            VerticalPager(
                state = datesPagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .height(370.dp)
                    .fillMaxWidth(),
            ) { page ->

                var showContent by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = datesPagerState.isScrollInProgress) {
                    snapshotFlow {
                        Pair(
                            datesPagerState.isScrollInProgress,
                            abs(datesPagerState.settledPage - page)
                        )
                    }.collect { (scrollInProgress, diff) ->
                        if (!scrollInProgress && (diff in 0..1)) {
                            showContent = true
                            cancel()
                        }
                    }
                }
                if (showContent) {
                    BookmarkCalendarMonthView(
                        page = page,
                        updateSelectedDate = updateSelectedDate,
                        calendarEventsByDate = calendarEventsByDate,
                        todaysDate = todaysDate,
                        selectedDate = selectedDate,
                    )
                } else {
                    Box(modifier = Modifier.padding(top = 10.dp)) {
                        CustomProgressIndicator()
                    }
                }
            }
        }
        item {
            BottomSheet(
                onEventSelection = onEventSelection,
                selectedDate = selectedDate,
                calendarEventsByDate = calendarEventsByDate,
            )
        }
        item {
            Spacer(modifier = Modifier.height(68.dp))
        }
    }

    LaunchedEffect(datesPagerState.isScrollInProgress) {
        snapshotFlow { datesPagerState.currentPageOffsetFraction }.collect {
            monthTitlePagerState.scrollToPage(
                datesPagerState.currentPage,
                datesPagerState.currentPageOffsetFraction
            )
        }
    }

    LaunchedEffect(monthTitlePagerState.isScrollInProgress) {
        snapshotFlow { monthTitlePagerState.currentPageOffsetFraction }.collect {
            datesPagerState.scrollToPage(
                monthTitlePagerState.currentPage,
                monthTitlePagerState.currentPageOffsetFraction
            )
        }
    }
}

@Composable
fun MonthName(
    page: Int,
) {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, page)
    Text(
        text = month_date.format(calendar.time),
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    )
}

@Composable
fun DaysOfTheWeek() {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        DaysOfWeek.entries.forEach {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = it.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            }
        }
    }
}

enum class DaysOfWeek() {
    Mon, Tur, Wed, Thu, Fri, Sat, Sun
}