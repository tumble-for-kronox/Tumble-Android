package tumble.app.tumble.presentation.views.account.User.ResourceSection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.AccountViewModel

@Composable
fun Resources(
    parentViewModel: AccountViewModel = hiltViewModel(),
    getResourcesAndEvents: () -> Unit,
    collapsedHeader: MutableState<Boolean>,
    navController: NavHostController
){
    val scrollState = rememberScrollState()
    val coroutinScope = rememberCoroutineScope()
    var scrollOffset by remember {
        mutableStateOf(0f)
    }
    var showingConfirmationDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        getResourcesAndEvents()
    }

    val combinedData = parentViewModel.combinedData.collectAsState()
    val userBookings = parentViewModel.userBookings.collectAsState()
    val userEvents = parentViewModel.completeUserEvent.collectAsState()

    val registeredBookingsState = parentViewModel.registeredBookingsSectionState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colors.background)
    ) {
        //TODO pull to refresh

        ResourceSectionDivider(title = "User options"){
            Switch(
                checked = combinedData.value.autoSignup?: false,
                onCheckedChange = {
                    if(it){
                        showingConfirmationDialog = true
                    }else{
                        parentViewModel.toggleAutoSignup(false)
                    }
                },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
            )

            if(showingConfirmationDialog){
                AlertDialog(
                    onDismissRequest = { showingConfirmationDialog = false },
                    title = { Text(text = "Confirm Action")},
                    text = { Text(text = "are you sure you want to enable this experimental feature?")},
                    confirmButton = {
                        TextButton(onClick = {
                            parentViewModel.toggleAutoSignup(true)
                            showingConfirmationDialog = false
                        }) {
                            Text(text = "Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            parentViewModel.toggleAutoSignup(false)
                            showingConfirmationDialog = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
        ResourceSectionDivider(
            title = "Your bookings",
            resourceType = ResourceType.RESOURCE,
            destination = { navController.navigate(Routes.accountResources) }
        ) {
            RegisteredBooking(
                onClickResource = {  },
                state = registeredBookingsState,
                bookings = userBookings.value?.bookings?: emptyList()
            )
        }
        ResourceSectionDivider(
            title = "Your event",
            resourceType = ResourceType.EVENT,
            destination = { navController.navigate(Routes.accountEvents) }
        ) {
            RegisteredEvent(
                onClickEvent = { },
                state = parentViewModel.registeredEventSectionState.collectAsState(),
                registeredEvents = userEvents.value?.registeredEvents?: emptyList()
            )
        }
    }
    LaunchedEffect(key1 = scrollState.value) {

        scrollOffset = scrollState.value.toFloat()

        if(scrollOffset >= 80){
            coroutinScope.launch {
                collapsedHeader.value = true
            }
        } else{
            coroutinScope.launch {
                collapsedHeader.value = false
            }
        }
    }
}


fun onClickResource(resource: NetworkResponse.KronoxUserBookingElement){

}


fun onClickEvent(resource: NetworkResponse.KronoxUserBookingElement){

}