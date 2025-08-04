package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDatePicker(
    onDateChange: (Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                dayContentColor = MaterialTheme.colorScheme.onBackground,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                weekdayContentColor = MaterialTheme.colorScheme.primary,
                todayContentColor = MaterialTheme.colorScheme.onBackground,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                yearContentColor = MaterialTheme.colorScheme.onBackground,
                subheadContentColor = Color.Red,
                containerColor = MaterialTheme.colorScheme.background,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                currentYearContentColor = MaterialTheme.colorScheme.onBackground,
            ),
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