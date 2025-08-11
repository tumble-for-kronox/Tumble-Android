package com.tumble.kronoxtoapp.presentation.screens.account.user.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tumble.kronoxtoapp.domain.models.TumbleUser
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.Resources
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.sheets.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.sheets.ResourceDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountDataState
import kotlinx.coroutines.launch

@Composable
fun UserOverview(
    user: TumbleUser,
    schoolName: String?,
    resetTopNavState: () -> Unit,
    bookingsState: AccountDataState,
    eventsState: AccountDataState,
    onClickResource: (NetworkResponse.KronoxUserBookingElement) -> Unit,
    onClickEvent: (NetworkResponse.AvailableKronoxUserEvent) -> Unit,
    onConfirmBooking: (String, String) -> Unit,
    onUnbookResource: (String) -> Unit,
    onLoadUserEvents: () -> Unit,
    onLoadUserBookings: () -> Unit,
    selectedBooking: NetworkResponse.KronoxUserBookingElement?,
    selectedEvent: NetworkResponse.AvailableKronoxUserEvent?,
    onClearSelectedBooking: () -> Unit,
    onClearSelectedEvent: () -> Unit,
    setTopNavState: (AppBarState) -> Unit,
    navController: NavHostController
) {
    val collapsedHeader = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollOffset by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background),
    ) {
        // User Profile Section
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(10.dp)
                .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
            ) {
                UserAvatar(name = user.name, collapsedHeader = collapsedHeader.value)
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = user.name,
                        fontSize = if (collapsedHeader.value) 20.sp else 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!collapsedHeader.value) {
                        Text(
                            text = user.username,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Text(
                            text = schoolName ?: "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        // Resources Section
        Resources(
            bookingsState = bookingsState,
            eventsState = eventsState,
            onClickResource = onClickResource,
            onClickEvent = onClickEvent,
            onConfirmBooking = onConfirmBooking,
            onUnbookResource = onUnbookResource,
            onLoadResourcesAndEvents = {
                onLoadUserEvents()
                onLoadUserBookings()
            },
            navController = navController
        )
    }

    ResourceBottomSheet(
        booking = selectedBooking,
        onDismiss = {
            onClearSelectedBooking()
            resetTopNavState()
        },
        onUnbook = { bookingId ->
            onUnbookResource(bookingId)
            onClearSelectedBooking()
        },
        onConfirm = { resourceId, bookingId ->
            onConfirmBooking(resourceId, bookingId)
        },
        setTopNavState = setTopNavState
    )

    EventBottomSheet(
        event = selectedEvent,
        onDismiss = {
            onClearSelectedEvent()
            resetTopNavState()
        },
        setTopNavState = setTopNavState
    )

    LaunchedEffect(key1 = scrollState.value) {
        scrollOffset = scrollState.value.toFloat()
        if (scrollOffset >= 80) {
            coroutineScope.launch {
                collapsedHeader.value = true
            }
        } else if (scrollOffset == 0f) {
            coroutineScope.launch {
                collapsedHeader.value = false
            }
        }
    }
}

@Composable
private fun ResourceBottomSheet(
    booking: NetworkResponse.KronoxUserBookingElement?,
    onDismiss: () -> Unit,
    onUnbook: (String) -> Unit,
    onConfirm: (String, String) -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    val slideTransition = slideInVertically(initialOffsetY = { it }) to
            slideOutVertically(targetOffsetY = { it })

    AnimatedVisibility(
        visible = booking != null,
        enter = fadeIn() + slideTransition.first,
        exit = fadeOut() + slideTransition.second
    ) {
        booking?.let {
            ResourceDetailsSheet(
                booking = it,
                onDismiss = onDismiss,
                onUnbook = { onUnbook(it.id) },
                onConfirm = onConfirm,
                setTopNavState = setTopNavState
            )
        }
    }
}

@Composable
private fun EventBottomSheet(
    event: NetworkResponse.AvailableKronoxUserEvent?,
    onDismiss: () -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    val slideTransition = slideInVertically(initialOffsetY = { it }) to
            slideOutVertically(targetOffsetY = { it })

    AnimatedVisibility(
        visible = event != null,
        enter = fadeIn() + slideTransition.first,
        exit = fadeOut() + slideTransition.second
    ) {
        event?.let {
            EventDetailsSheet(
                event = it,
                onDismiss = onDismiss,
                setTopNavState = setTopNavState
            )
        }
    }
}