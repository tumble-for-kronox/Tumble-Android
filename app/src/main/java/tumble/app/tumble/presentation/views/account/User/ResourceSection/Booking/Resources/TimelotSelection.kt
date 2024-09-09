package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.presentation.views.general.Info
import java.time.LocalDate
import java.util.Date

enum class BookingButtonState {
    LOADING,
    BOOKED,
    AVAILABLE
}

@Composable
fun TimeslotSelection(
    resourceId: String,
    bookResource: (String, Date, NetworkResponse.AvailabilityValue) -> Boolean,
    selectedPickerDate: Date,
    availabilityValues: MutableState<List<NetworkResponse.AvailabilityValue>>
) {
    val buttonStateMap = remember { mutableMapOf<String, BookingButtonState>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(availabilityValues.value) {
        setupButtons(availabilityValues, buttonStateMap)
    }

    if (availabilityValues.value.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Info(title = "No available timeslots", image = null)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            availabilityValues.value.forEach { availabilityValue ->
                availabilityValue.locationID?.let { locationId ->
                    TimeslotCard(
                        onBook = { bookResource(resourceId, selectedPickerDate, availabilityValue) },
                        locationId = locationId,
                        bookingButtonState = buttonStateMap[locationId] ?: BookingButtonState.AVAILABLE
                    )
                }
            }
        }
    }
}

private fun setupButtons(
    availabilityValues: MutableState<List<NetworkResponse.AvailabilityValue>>,
    buttonStateMap: MutableMap<String, BookingButtonState>
) {
    availabilityValues.value.forEach { availabilityValue ->
        availabilityValue.locationID?.let { locationId ->
            buttonStateMap[locationId] = BookingButtonState.AVAILABLE
        }
    }
}

private fun handleBooking(
    locationId: String,
    availabilityValue: NetworkResponse.AvailabilityValue,
    resourceId: String,
    selectedPickerDate: LocalDate,
    bookResource: suspend (String, LocalDate, NetworkResponse.AvailabilityValue) -> Boolean,
    buttonStateMap: MutableMap<String, BookingButtonState>,
    scope: CoroutineScope
) {
    buttonStateMap[locationId] = BookingButtonState.LOADING
    scope.launch {
        val result = withContext(Dispatchers.IO) {
            bookResource(resourceId, selectedPickerDate, availabilityValue)
        }
        buttonStateMap[locationId] = if (result) BookingButtonState.BOOKED else BookingButtonState.AVAILABLE
    }
}
