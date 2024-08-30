package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.presentation.ResourceSelectionModel
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator
import tumble.app.tumble.presentation.views.general.Info
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ResourceBookings(
    viewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController
) {
    val resourceBookingPageState = viewModel.resourceBookingPageState.collectAsState()
    val selectedPikerDate = viewModel.selectedPickerDate.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background) // Replace with your theme's background color
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            CloseCoverButton {
                navController.popBackStack()
            }
        }  
    
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            ResourceDatePicker(
                selectedDate = selectedPikerDate.value,
                onDateChange = { date ->
                    viewModel.setBookingDate(date)
                }
            )
            Divider(color = MaterialTheme.colors.onBackground) // Replace with your theme's divider color

            when (resourceBookingPageState.value) {
                PageState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomProgressIndicator()
                    }
                }
                PageState.LOADED -> {
                    ResourceLocationsList(
                        parentViewModel = viewModel,
                        selectedPickerDate = selectedPikerDate.value,
                        navigateToResourceSelection = { resource, date -> viewModel.resourceSelectionModel =  ResourceSelectionModel(resource,date)}
                    )
                }
                PageState.ERROR -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isWeekend(selectedPikerDate.value)) {
                            Info(
                                title = "No rooms available on weekends",
                                image = null//R.drawable.moon_zzz // Replace with your drawable resource
                            )
                        } else {
                            Info(
                                title = "Could not contact the server, try again later",
                                image = null //R.drawable.arrow_clockwise // Replace with your drawable resource
                            )
                        }
                    }
                }
            }
        }
    }

    viewModel.resourceSelectionModel?.let {
        ResourceSelection()
    }


    LaunchedEffect(viewModel.selectedPickerDate) {
        viewModel.getAllResourceData(selectedPikerDate.value)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isWeekend(date: Date): Boolean{
    val calendar = Calendar.getInstance().apply { time = date }
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    return day == 1 || day  == 7
}

