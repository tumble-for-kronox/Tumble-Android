package com.tumble.kronoxtoapp.presentation.screens.bookmarks.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.domain.models.realm.Day
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.models.sorted
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookmarkListView(
    days: List<Day>,
    onEventSelection: (Event) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
                items(days.size) { day ->
                    DayItem(days[day], onEventSelection = onEventSelection)
                }
                item {
                    Spacer(modifier = Modifier.height(68.dp))
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 15.dp, end = 7.5.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                ToTopButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun DayItem(
    day: Day, onEventSelection: (Event) -> Unit
) {
    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        DayHeader(day)
        EventList(events = day.events?.toList() ?: listOf(), onEventSelection)
    }
}

@Composable
fun EventList(events: List<Event>, onEventSelection: (Event) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val sortedEvents = events.sorted()
        sortedEvents.forEach { event ->
            BookmarkCard(
                onTapCard = { onEventSelection(it) },
                event = event,
                isLast = false
            )
        }
    }
}