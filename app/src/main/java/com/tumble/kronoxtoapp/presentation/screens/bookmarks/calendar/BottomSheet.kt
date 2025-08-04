package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.extensions.presentation.toColor
import com.tumble.kronoxtoapp.presentation.components.buttons.CompactEventButtonLabel
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.realm.Event


@Composable
fun BottomSheet(
    viewModel: BookmarksViewModel = hiltViewModel(),
    onEventSelection: (Event) -> Unit,
) {
    viewModel.selectedDate?.let {
        val events = viewModel.bookmarkData.calendarEventsByDate.getOrDefault(it, emptyList())
        if (events.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_events_for_this_day),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                events.forEach { event ->
                    CompactEventButtonLabel(
                        event = event,
                        color = event.course?.color?.toColor() ?: Color.Red,
                        onEventSelection = onEventSelection
                    )
                }
            }
        }
    }
}