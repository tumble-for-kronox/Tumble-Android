package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.domain.models.realm.Event
import java.time.LocalDate


@Composable
fun dateRow(
    localDate: LocalDate,
    calendarEventsByDate: Map<LocalDate, List<Event>>,
    getBackgroundColor: @Composable (LocalDate) -> Color,
    getOnBackgroundColor: @Composable (LocalDate) -> Color,
    onClick: (LocalDate) -> Unit
): LocalDate {

    var localLocalDate = localDate
    Row (horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        repeat(7) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalendarDate(
                    localDate = localLocalDate,
                    getBackgroundColor = getBackgroundColor,
                    getOnBackgroundColor = getOnBackgroundColor,
                    onClick = onClick
                )
                EventIndicator(
                    localDate = localLocalDate,
                    calendarEventsByDate = calendarEventsByDate
                )
            }
            localLocalDate = localLocalDate.plusDays(1)
        }
    }
    return localLocalDate
}