package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.presentation.noRippleClickable
import com.tumble.kronoxtoapp.other.extensions.presentation.toColor
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarkData
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import java.time.LocalDate

@Composable
fun CalendarDate(
    localDate: LocalDate,
    getBackgroundColor: @Composable (LocalDate) -> Color,
    getOnBackgroundColor: @Composable (LocalDate) -> Color,
    onClick: (LocalDate) -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Background(
            localDate = localDate,
            onClick = onClick,
            getColor = getBackgroundColor
        )
        Text(
            text = localDate.dayOfMonth.toString(),
            color = getOnBackgroundColor(localDate),
            fontSize = 20.sp
        )
    }
}

@Composable
fun Background(
    localDate: LocalDate,
    onClick: (LocalDate) -> Unit,
    getColor: @Composable (LocalDate) -> Color,
) {

    Surface(
        modifier = Modifier
            .size(40.dp)
            .noRippleClickable { onClick(localDate) },
        shape = CircleShape,
        color = getColor(localDate)
    ) {}
}

@Composable
fun EventIndicator(
    calendarEventsByDate: Map<LocalDate, List<Event>>,
    localDate: LocalDate
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 2.dp)
            .height(6.dp)
    ) {

        val events = calendarEventsByDate.getOrDefault(localDate, emptyList())
        if (events.isNotEmpty()) {
            events.forEach {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(6.dp)
                        .background(
                            color = it.course?.color?.toColor()
                                ?: MaterialTheme.colorScheme.primary, CircleShape
                        )
                )
            }
        }
    }
}