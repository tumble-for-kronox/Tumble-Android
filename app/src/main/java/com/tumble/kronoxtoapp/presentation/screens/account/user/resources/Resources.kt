package com.tumble.kronoxtoapp.presentation.screens.account.user.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountViewModel

@Composable
fun Resources(
    parentViewModel: AccountViewModel = hiltViewModel(),
    getResourcesAndEvents: () -> Unit,
    navController: NavHostController
){
    LaunchedEffect(key1 = Unit) {
        getResourcesAndEvents()
    }
    val userBookings = parentViewModel.userBookings.collectAsState()
    val userEvents = parentViewModel.completeUserEvent.collectAsState()
    val registeredBookingsState = parentViewModel.registeredBookingsSectionState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ResourceSectionDivider(
            title = stringResource(R.string.user_booking),
            resourceType = ResourceType.RESOURCE,
            destination = { navController.navigate(Routes.accountResources) }
        ) {
            RegisteredBookings(
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
            RegisteredEvents(
                onClickEvent = { parentViewModel.setEvent(it) },
                state = parentViewModel.registeredEventSectionState.collectAsState(),
                registeredEvents = userEvents.value?.registeredEvents?: emptyList()
            )
        }
    }
}