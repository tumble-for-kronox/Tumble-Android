package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.other.extensions.models.getAvailabilityValues
import com.tumble.kronoxtoapp.other.extensions.models.getFirstTimeSlotWithAvailability
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceBookingState
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceSelectionViewModel
import com.tumble.kronoxtoapp.utils.isoVerboseDateFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ResourceSelection(
    viewModel: ResourceSelectionViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit,
    resourceId: String,
    selectedPickerDate: Date
) {
    val pageTitle = stringResource(R.string.rooms)
    val backTitle = stringResource(R.string.resources)

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = pageTitle,
                navigationAction = {
                    BackButton(backTitle) {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (viewModel.resourceBookingState) {
            is ResourceBookingState.Loading -> {
                CustomProgressIndicator()
            }

            is ResourceBookingState.Error -> {
                val errorMessage =
                    (viewModel.resourceBookingState as ResourceBookingState.Error).message
                Info(errorMessage)
            }

            is ResourceBookingState.Loaded -> {
                val resource =
                    (viewModel.resourceBookingState as ResourceBookingState.Loaded).resource
                val timeslots = resource.timeSlots
                ResourceAvailabilities(
                    selectedPickerDate = selectedPickerDate,
                    resource = resource,
                    viewModel = viewModel,
                    timeSlots = timeslots
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getResource(resourceId, selectedPickerDate)
    }
}

@Composable
private fun ResourceAvailabilities(
    selectedPickerDate: Date,
    resource: NetworkResponse.KronoxResourceElement,
    viewModel: ResourceSelectionViewModel,
    timeSlots: List<NetworkResponse.TimeSlot>?
) {
    val scope = rememberCoroutineScope()
    val selectedTimeIndex = remember {
        mutableIntStateOf(
            resource.availabilities.getFirstTimeSlotWithAvailability(resource.timeSlots!!.size)
        )
    }
    val availabilityValues = remember {
        mutableStateOf(resource.availabilities.getAvailabilityValues(timeslotId = selectedTimeIndex.intValue))
    }

    Row {
        Text(
            text = isoVerboseDateFormatter.format(selectedPickerDate),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 20.dp, bottom = 5.dp)
                .fillMaxWidth(),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    if (timeSlots != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            TimeslotDropdown(
                resource = resource,
                timeslots = timeSlots,
                selectedIndex = selectedTimeIndex,
                onIndexChange = { index ->
                    selectedTimeIndex.intValue = index
                    availabilityValues.value =
                        resource.availabilities.getAvailabilityValues(timeslotId = index)
                }
            )
        }
        HorizontalDivider()
        TimeslotSelection(
            bookResource = { availabilityValue, onResult ->
                resource.id?.let { resourceId ->
                    scope.launch {
                        try {
                            val success = viewModel.bookResource(
                                resourceId = resourceId,
                                date = selectedPickerDate,
                                availabilityValue = availabilityValue
                            )
                            onResult(success)
                        } catch (e: Exception) {
                            onResult(false)
                        }
                    }
                } ?: onResult(false)
            },
            availabilityValues = availabilityValues
        )
    } else {
        Info(
            title = stringResource(R.string.no_timeslots),
            image = null,
        )
    }
}