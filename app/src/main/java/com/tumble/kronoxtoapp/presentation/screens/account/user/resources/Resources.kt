package com.tumble.kronoxtoapp.presentation.screens.account.user.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountDataState

@Composable
fun Resources(
    bookingsState: AccountDataState,
    eventsState: AccountDataState,
    onClickResource: (NetworkResponse.KronoxUserBookingElement) -> Unit,
    onClickEvent: (NetworkResponse.AvailableKronoxUserEvent) -> Unit,
    onConfirmBooking: (String, String) -> Unit,
    onUnbookResource: (String) -> Unit,
    onLoadResourcesAndEvents: () -> Unit,
    navController: NavHostController
) {
    LaunchedEffect(key1 = Unit) {
        onLoadResourcesAndEvents()
    }

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
                onClickResource = onClickResource,
                bookingsState = bookingsState,
                confirmBooking = onConfirmBooking,
                unBook = onUnbookResource
            )
        }

        ResourceSectionDivider(
            title = stringResource(R.string.user_events),
            resourceType = ResourceType.EVENT,
            destination = { navController.navigate(Routes.accountEvents) }
        ) {
            RegisteredEvents(
                onClickEvent = onClickEvent,
                eventsState = eventsState
            )
        }
    }
}