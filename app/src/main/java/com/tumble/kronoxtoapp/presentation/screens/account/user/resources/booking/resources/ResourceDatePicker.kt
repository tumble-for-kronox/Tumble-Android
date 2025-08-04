package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDatePicker(
    onDateChange: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val starOfDay = remember {
        mutableStateOf(Calendar.getInstance().apply { set(Calendar.MILLISECONDS_IN_DAY, 0)})
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                selectedDayContentColor = MaterialTheme.colorScheme.onBackground,
                dayContentColor = MaterialTheme.colorScheme.onBackground,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                weekdayContentColor = MaterialTheme.colorScheme.primary,
                todayContentColor = MaterialTheme.colorScheme.onBackground,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                yearContentColor = MaterialTheme.colorScheme.onBackground,
                subheadContentColor = Color.Red,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                currentYearContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            dateValidator = { Calendar.getInstance().apply { timeInMillis = it }.after(starOfDay.value) },
            title = null,
            headline = null,
        )
    }
    LaunchedEffect(key1 = datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            onDateChange(Date(it))
        }
    }
}