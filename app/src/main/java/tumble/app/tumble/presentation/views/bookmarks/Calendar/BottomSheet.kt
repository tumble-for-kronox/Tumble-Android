package tumble.app.tumble.presentation.views.bookmarks.Calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.presentation.components.buttons.CompactEventButtonLabel
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event

@RequiresApi(Build.VERSION_CODES.O)
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
                    color = MaterialTheme.colors.onBackground,
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