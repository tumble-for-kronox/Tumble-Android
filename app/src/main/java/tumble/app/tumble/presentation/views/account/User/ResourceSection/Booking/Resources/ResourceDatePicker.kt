package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.dp
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
            .background(color = MaterialTheme.colors.background)
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                selectedDayContentColor = MaterialTheme.colors.onBackground,
                dayContentColor = MaterialTheme.colors.onBackground,
                selectedDayContainerColor = MaterialTheme.colors.primary,
                weekdayContentColor = MaterialTheme.colors.primary,
                todayContentColor = MaterialTheme.colors.onBackground,
                todayDateBorderColor = MaterialTheme.colors.primary,
                yearContentColor = MaterialTheme.colors.onBackground,
                subheadContentColor = Color.Red,
                selectedYearContainerColor = MaterialTheme.colors.primary,
                selectedYearContentColor = MaterialTheme.colors.onPrimary,
                currentYearContentColor = MaterialTheme.colors.onBackground,
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