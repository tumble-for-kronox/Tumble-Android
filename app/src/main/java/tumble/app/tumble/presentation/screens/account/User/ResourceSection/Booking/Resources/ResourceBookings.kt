package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Resources

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.presentation.ResourceSelectionModel
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.navigation.UriBuilder
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.general.Info
import tumble.app.tumble.presentation.screens.navigation.AppBarState
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ResourceBookings(
    viewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.resources)
    val backTitle = stringResource(R.string.account)

    val resourceBookingPageState = viewModel.resourceBookingPageState.collectAsState()
    val selectedPikerDate = viewModel.selectedPickerDate.collectAsState()

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
        ResourceDatePicker(
            onDateChange = { date ->
                viewModel.setBookingDate(date)
            }
        )
        Divider()

        when (resourceBookingPageState.value) {
            PageState.LOADING -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomProgressIndicator()
                }
            }
            PageState.LOADED -> {
                ResourceLocationsList(
                    parentViewModel = viewModel,
                    selectedPickerDate = selectedPikerDate.value,
                    navigateToResourceSelection = { resource, date ->
                        AppController.shared.resourceModel = ResourceSelectionModel(resource, date)
                        navController.navigate(UriBuilder.buildAccountResourceDetailsUri(resource.id.toString()).toUri())
                    }
                )
            }
            PageState.ERROR -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isWeekend(selectedPikerDate.value)) {
                        Info(
                            title = stringResource(R.string.no_rooms_on_weekend),
                            icon = Icons.Default.Bedtime
                        )
                    } else {
                        Info(
                            title = stringResource(R.string.could_not_contact_server),
                            icon = Icons.Default.ErrorOutline
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(viewModel.selectedPickerDate) {
        viewModel.getAllResources(selectedPikerDate.value)
    }
}

fun isWeekend(date: Date): Boolean{
    val calendar = Calendar.getInstance().apply { time = date }
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    return day == 1 || day  == 7
}

