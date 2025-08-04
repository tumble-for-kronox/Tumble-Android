package com.tumble.kronoxtoapp.presentation.screens.bookmarks.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.presentation.viewmodels.BookmarksViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookmarkCalendarMonthView(
    viewModel: BookmarksViewModel = hiltViewModel(),
    page: Int,
    ){

    val date = LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)

    val localDate = date.plusMonths(page.toLong())

    val inMonth = { int: Int -> if (int == localDate.month.value) 1f else 0.5f}
    val onClick =  {localDat: LocalDate -> viewModel.updateSelectedDate(localDat)}
    val getColor = @Composable { localDat: LocalDate -> viewModel.getColor(date = localDat)}
    var currentDate = localDate.minusDays((localDate.dayOfWeek.value-1).toLong())

    Column {
        repeat(6) {
            currentDate = dateRow(
                inMonth = inMonth,
                localDate = currentDate,
                getColor = getColor,
                onClick = onClick
            )
        }
    }
}
