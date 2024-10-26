package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.models.getAvailabilityValues
import tumble.app.tumble.extensions.models.getFirstTimeSlotWithAvailability
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.utils.isoVerboseDateFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResourceSelection(
    parentViewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController
) {
    val resource = AppController.shared.resourceModel!!.resource
    val selectedTimeIndex = remember { mutableStateOf(resource.availabilities.getFirstTimeSlotWithAvailability(resource.timeSlots!!.size)) }
    val availabilityValues = remember {
        mutableStateOf<List<NetworkResponse.AvailabilityValue>>(resource.availabilities.getAvailabilityValues(timelotId = selectedTimeIndex.value))
    }
    val selectedPickerDate = AppController.shared.resourceModel!!.date
    val timeslots = resource.timeSlots
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Row {
            Text(
                text = isoVerboseDateFormatter.format(selectedPickerDate),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        if (timeslots != null) {
            TimeslotDropdown(
                resource = resource,
                timeslots = timeslots,
                selectedIndex = selectedTimeIndex,
                onIndexChange = { index ->
                    selectedTimeIndex.value = index
                    availabilityValues.value =
                        resource.availabilities.getAvailabilityValues(timelotId = index)
                }
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
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
