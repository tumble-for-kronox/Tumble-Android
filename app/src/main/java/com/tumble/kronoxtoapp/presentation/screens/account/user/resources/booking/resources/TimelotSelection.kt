package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.screens.general.Info

enum class BookingButtonState {
    LOADING,
    BOOKED,
    AVAILABLE
}

@Composable
fun TimeslotSelection(
    bookResource: (NetworkResponse.AvailabilityValue, onResult: (Boolean) -> Unit) -> Unit,
    availabilityValues: MutableState<List<NetworkResponse.AvailabilityValue>>
) {
    val buttonStateMap = remember { mutableStateMapOf<String, BookingButtonState>() }

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
            Info(title = stringResource(R.string.no_timeslots), image = null)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            availabilityValues.value
                .filter { it.availability == NetworkResponse.AvailabilityEnum.AVAILABLE }
                .forEach { availabilityValue ->
                    availabilityValue.locationId?.let { locationId ->
                        TimeslotCard(
                            onBook = {
                                handleBooking(
                                    locationId = locationId,
                                    availabilityValue = availabilityValue,
                                    bookResource = bookResource,
                                    buttonStateMap = buttonStateMap
                                )
                            },
                            locationId = locationId,
                            bookingButtonState = buttonStateMap[locationId]
                                ?: BookingButtonState.AVAILABLE
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
        availabilityValue.locationId?.let { locationId ->
            buttonStateMap[locationId] = BookingButtonState.AVAILABLE
        }
    }
}

private fun handleBooking(
    locationId: String,
    availabilityValue: NetworkResponse.AvailabilityValue,
    bookResource: (NetworkResponse.AvailabilityValue, onResult: (Boolean) -> Unit) -> Unit,
    buttonStateMap: MutableMap<String, BookingButtonState>
) {
    buttonStateMap[locationId] = BookingButtonState.LOADING

    bookResource(availabilityValue) { success ->
        buttonStateMap[locationId] =
            if (success) BookingButtonState.BOOKED else BookingButtonState.AVAILABLE
    }
}