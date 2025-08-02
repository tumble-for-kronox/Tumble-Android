package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Events

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
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.SectionDivider
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.general.Info
import tumble.app.tumble.presentation.screens.navigation.AppBarState

@RequiresApi(Build.VERSION_CODES.O)
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
                                Events(
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
                                Events(
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
                                Events(upcomingEvents = completeUserEvent.value?.upcomingEvents)
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