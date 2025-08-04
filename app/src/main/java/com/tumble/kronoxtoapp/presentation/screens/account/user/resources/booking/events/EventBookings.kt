package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.AvailableKronoxUserEvent
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse.UpcomingKronoxUserEvent
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceViewModel
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.SectionDivider
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

// Use fake data temporarily for UI development
val fakeDummyRegisteredEvents = listOf(
    AvailableKronoxUserEvent(
        id = "evt-001",
        title = "Morning Yoga Session",
        type = "Fitness",
        eventStart = "2025-08-05T08:00:00",
        eventEnd = "2025-08-05T09:00:00",
        lastSignupDate = "2025-08-04T20:00:00",
        participatorId = "user123",
        supportId = null,
        anonymousCode = "REG123",
        isRegistered = true,
        supportAvailable = true,
        requiresChoosingLocation = false
    ),
    AvailableKronoxUserEvent(
        id = "evt-003",
        title = "Weekly Team Meeting",
        type = "Meeting",
        eventStart = "2025-08-07T09:00:00",
        eventEnd = "2025-08-07T10:00:00",
        lastSignupDate = "2025-08-06T17:00:00",
        participatorId = "user123",
        supportId = null,
        anonymousCode = "REG789",
        isRegistered = true,
        supportAvailable = false,
        requiresChoosingLocation = false
    )
)

val fakeDummyUnregisteredEvents = listOf(
    AvailableKronoxUserEvent(
        id = "evt-002",
        title = "Introduction to Kotlin Programming",
        type = "Lecture",
        eventStart = "2025-08-06T10:00:00",
        eventEnd = "2025-08-06T11:30:00",
        lastSignupDate = "2025-08-05T18:00:00",
        participatorId = null,
        supportId = "support789",
        anonymousCode = "UNREG456",
        isRegistered = false,
        supportAvailable = false,
        requiresChoosingLocation = true
    ),
    AvailableKronoxUserEvent(
        id = "evt-004",
        title = "Advanced Android Development Workshop",
        type = "Workshop",
        eventStart = "2025-08-08T13:00:00",
        eventEnd = "2025-08-08T16:00:00",
        lastSignupDate = "2025-08-07T12:00:00",
        participatorId = null,
        supportId = null,
        anonymousCode = "UNREG999",
        isRegistered = false,
        supportAvailable = true,
        requiresChoosingLocation = false
    )
)

val fakeDummyUpcomingEvents = listOf(
    UpcomingKronoxUserEvent(
        title = "Tech Meetup",
        type = "Networking",
        eventStart = "2025-08-10T14:00:00",
        eventEnd = "2025-08-10T17:00:00",
        firstSignupDate = "2025-08-01T09:00:00"
    ),
    UpcomingKronoxUserEvent(
        title = "Design Thinking Workshop",
        type = "Workshop",
        eventStart = "2025-08-12T10:00:00",
        eventEnd = "2025-08-12T15:00:00",
        firstSignupDate = "2025-08-03T08:00:00"
    )
)


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EventBookings(
    viewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.events)
    val backTitle = stringResource(R.string.account)
    val completeUserEvent = viewModel.completeUserEvent.collectAsState()
    val eventBookingPageState = viewModel.eventBookingPageState.collectAsState()

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

    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            when (eventBookingPageState.value) {
                PageState.LOADING -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomProgressIndicator()
                    }
                }
                PageState.LOADED -> {
                    SectionDivider(
                        title = stringResource(R.string.registered),
                        image = Icons.Default.PersonAddAlt1,
                        content = {
                            if (completeUserEvent.value?.registeredEvents != null) {
                                RegisteredEventsView(
                                    registeredEvents = completeUserEvent.value?.registeredEvents,
                                    onTapEventAction = { eventId, eventType ->
                                        onTapEventAction(viewModel, eventId, eventType)
                                    }
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.no_registered_events),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                    SectionDivider(
                        title = stringResource(R.string.unregistered),
                        image = Icons.Default.PersonRemove,
                        content = {
                            if (completeUserEvent.value?.unregisteredEvents != null) {
                                UnregisteredEventsView(
                                    unregisteredEvents = completeUserEvent.value?.unregisteredEvents,
                                    onTapEventAction = { eventId, eventType ->
                                        onTapEventAction(viewModel, eventId, eventType)
                                    }
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.no_unregistered_events),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                    SectionDivider(
                        title = stringResource(R.string.upcoming),
                        image = Icons.Default.PersonSearch,
                        content = {
                            if (completeUserEvent.value?.upcomingEvents != null) {
                                UpcomingEventsView(upcomingEvents = completeUserEvent.value?.upcomingEvents)
                            } else {
                                Text(
                                    text = stringResource(R.string.no_upcoming_events),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                }
                PageState.ERROR -> {
                    Info(
                        title = stringResource(R.string.server_error),
                        image = null,
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            viewModel.getUserEventsForPage()
        }
    }
}



fun onTapEventAction(viewModel: ResourceViewModel, eventId: String, eventType: EventType) {
    when (eventType) {
        EventType.REGISTER -> {
            viewModel.registerForEvent(eventId)
        }
        EventType.UNREGISTER -> {
            viewModel.unregisterForEvent(eventId)
        }
    }
}