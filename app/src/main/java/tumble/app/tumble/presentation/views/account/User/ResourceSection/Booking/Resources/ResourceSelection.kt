package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.models.getAvailabilityValues
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.general.Info
import java.time.LocalDate

@Composable
fun ResourceSelection(
    parentViewModel: ResourceViewModel = hiltViewModel(),
) {
    val selectedTimeIndex = remember { mutableStateOf(0) }
    val availabilityValues = remember {
        mutableStateOf<List<NetworkResponse.AvailabilityValue>>(emptyList())
    }
    val resource = parentViewModel.resourceSelectionModel!!.resource
    val selectedPickerDate = parentViewModel.resourceSelectionModel!!.date
    val timeslots = resource.timeSlots

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Replace with your background color
    ) {
        if (timeslots != null) {
            Text(
                text = selectedPickerDate.toString(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black, // Replace with your color
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .fillMaxWidth()
            )
            TimeslotDropdown(
                resource = resource,
                timeslots = timeslots,
                selectedIndex = selectedTimeIndex,
                onIndexChange = { index ->
                    selectedTimeIndex.value = index
                    availabilityValues.value = resource.availabilities.getAvailabilityValues(timelotId = index)
                }
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            TimeslotSelection(
                resourceId = resource.id!!,
                bookResource = { resourceId, date, availabilityValue ->
                    parentViewModel.bookResource(
                        resourceId = resourceId,
                        date = date,
                        availabilityValue = availabilityValue
                        )
                },
                selectedPickerDate = selectedPickerDate,
                availabilityValues = availabilityValues
            )
        } else {
            Info(
                title = "No available timeslots",
                image = null,
            )
        }
    }
}
