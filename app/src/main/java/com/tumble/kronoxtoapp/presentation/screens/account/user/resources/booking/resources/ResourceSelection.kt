package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.extensions.models.getAvailabilityValues
import com.tumble.kronoxtoapp.extensions.models.getFirstTimeSlotWithAvailability
import com.tumble.kronoxtoapp.observables.AppController
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceViewModel
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.utils.isoVerboseDateFormatter

@Composable
fun ResourceSelection(
    parentViewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit

) {
    val pageTitle = stringResource(R.string.rooms)
    val backTitle = stringResource(R.string.resources)

    val resource = AppController.shared.resourceModel!!.resource
    val selectedTimeIndex = remember { mutableIntStateOf(resource.availabilities.getFirstTimeSlotWithAvailability(resource.timeSlots!!.size)) }
    val availabilityValues = remember {
        mutableStateOf(resource.availabilities.getAvailabilityValues(timelotId = selectedTimeIndex.intValue))
    }
    val selectedPickerDate = AppController.shared.resourceModel!!.date
    val timeslots = resource.timeSlots
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
        if (timeslots != null) {
            // Center the dropdown with proper margins
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TimeslotDropdown(
                    resource = resource,
                    timeslots = timeslots,
                    selectedIndex = selectedTimeIndex,
                    onIndexChange = { index ->
                        selectedTimeIndex.intValue = index
                        availabilityValues.value =
                            resource.availabilities.getAvailabilityValues(timelotId = index)
                    }
                )
            }
            Divider()
            TimeslotSelection(
                bookResource = { availabilityValue ->
                    parentViewModel.bookResource(
                        resourceId = resource.id!!,
                        date = selectedPickerDate,
                        availabilityValue = availabilityValue
                    )
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
}
