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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.extensions.models.getAvailabilityValues
import tumble.app.tumble.extensions.models.getFirstTimeSlotWithAvailability
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.presentation.views.navigation.AppBarState
import tumble.app.tumble.utils.isoVerboseDateFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
            .background(MaterialTheme.colors.background)
    ) {
        Row {
            Text(
                text = isoVerboseDateFormatter.format(selectedPickerDate),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(top = 20.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        if (timeslots != null) {
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
