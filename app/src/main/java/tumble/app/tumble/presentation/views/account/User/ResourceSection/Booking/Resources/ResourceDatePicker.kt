package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import tumble.app.tumble.utils.isoDateFormatterDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDatePicker(
    selectedDate: Date,
    onDateChange: (Date) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val minDate = LocalDate.now().minusYears(1)

    val datePickerState = rememberDatePickerState()

    val showDatePickerDialog = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            //.background(MaterialTheme.colors.background) // Replace with your background color
            .height(300.dp)
    ) {
        OutlinedButton(
            onClick = { showDatePickerDialog.value = !showDatePickerDialog.value },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Pick a date: ${isoDateFormatterDate.format(selectedDate)}")
        }

        DatePicker(
            state = datePickerState,
            showModeToggle = showDatePickerDialog.value,
            //colors = DatePickerDefaults.colors(selectedDayContentColor = MaterialTheme.colors.primary)
        )
    }

    LaunchedEffect(key1 = datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            onDateChange(Date(it))
        }
    }

}