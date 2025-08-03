package tumble.app.tumble.presentation.screens.account.user.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import tumble.app.tumble.R
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
    LaunchedEffect(key1 = Unit) {
        getResourcesAndEvents()
    }
    val userBookings = parentViewModel.userBookings.collectAsState()
    val userEvents = parentViewModel.completeUserEvent.collectAsState()
    val registeredBookingsState = parentViewModel.registeredBookingsSectionState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ResourceSectionDivider(
            title = stringResource(R.string.user_booking),
            resourceType = ResourceType.RESOURCE,
            destination = { navController.navigate(Routes.accountResources) }
        ) {
            RegisteredBooking(
                onClickResource = { parentViewModel.setBooking(it) },
                state = registeredBookingsState,
                bookings = userBookings.value?.bookings?: emptyList(),
                confirmBooking = {resourceId, bookingId -> parentViewModel.confirmResource(resourceId, bookingId)},
                unBook = {bookingId -> parentViewModel.unBookResource(bookingId)}
            )
        }
        ResourceSectionDivider(
            title = stringResource(R.string.user_events),
            resourceType = ResourceType.EVENT,
            destination = { navController.navigate(Routes.accountEvents) }
        ) {
            RegisteredEvent(
                onClickEvent = { parentViewModel.setEvent(it) },
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
        } else if (scrollOffset == 0f){
            coroutinScope.launch {
                collapsedHeader.value = false
            }
        }
    }
}