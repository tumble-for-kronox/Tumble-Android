package tumble.app.tumble.presentation.views.bookmarks.List

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.models.presentation.EventDetailsSheetModel
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.models.sorted
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookmarkListView(
    viewModel: BookmarksViewModel = hiltViewModel(),
    onEventSelection: (Event) -> Unit = {Event -> }
){
    val days by remember {
       mutableStateOf(viewModel.bookmarkData.days)
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    //Log.e("eeee", "In list View")

    Scaffold(
        floatingActionButton = { ToTopButton { coroutineScope.launch { listState.animateScrollToItem(0) } } },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colors.background
    ) {padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            //Log.e("eeee", "In list View Col ")
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
            items(days.size) { day ->

                DayItem(day, onEventSelection = onEventSelection)
            }
            item{
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayItem(dayin: Int, onEventSelection: (Event) -> Unit,
            viewModel: BookmarksViewModel = hiltViewModel()){
    //Log.e("eeee","ReComp")
    Column(
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        val day = viewModel.bookmarkData.days.get(dayin)
        DayHeader(day)
        EventList(events = day.events?.toList()?: listOf(), onEventSelection )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventList(events: List<Event>, onEventSelection: (Event) -> Unit){
    Column (
        modifier = Modifier
    ){
        val sortedEvents = events.sorted()
        sortedEvents.forEach{event ->
            BookmarkCard(
                onTapCard = { onEventSelection(it) },
                event = event,
                isLast = false
            )
        }
    }
}