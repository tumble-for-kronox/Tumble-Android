package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import java.time.LocalDate


@Composable
fun BookmarkCalendarMonthView(
    updateSelectedDate: (LocalDate) -> Unit,
    calendarEventsByDate: Map<LocalDate, List<Event>>,
    todaysDate: LocalDate,
    selectedDate: LocalDate,
    page: Int
){

    val date = LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)

    val localDate = date.plusMonths(page.toLong())

    val onClick =  {localDat: LocalDate -> updateSelectedDate(localDat)}
    val getBackgroundColor = @Composable { passedLocalDate: LocalDate -> getBackgroundColor(todaysDate = todaysDate, selectedDate = selectedDate, date = passedLocalDate)}
    val getOnBackgroundColor = @Composable { passedLocalDate: LocalDate -> getOnBackgroundColor(todaysDate = todaysDate, selectedDate = selectedDate, date = passedLocalDate)}
    var currentDate = localDate.minusDays((localDate.dayOfWeek.value-1).toLong())

    Column {
        repeat(6) {
            currentDate = dateRow(
                calendarEventsByDate = calendarEventsByDate,
                localDate = currentDate,
                getBackgroundColor = getBackgroundColor,
                getOnBackgroundColor = getOnBackgroundColor,
                onClick = onClick
            )
        }
    }
}

@Composable
fun getBackgroundColor(todaysDate: LocalDate, selectedDate: LocalDate, date: LocalDate): Color {
    if (date == selectedDate){
        return MaterialTheme.colorScheme.primary
    }
    if (date == todaysDate){
        return MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    }
    return MaterialTheme.colorScheme.background
}

@Composable
fun getOnBackgroundColor(todaysDate: LocalDate, selectedDate: LocalDate, date: LocalDate): Color {
    if (date == selectedDate){
        return MaterialTheme.colorScheme.onPrimary
    }
    if (date == todaysDate){
        return MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    }
    return MaterialTheme.colorScheme.onBackground
}