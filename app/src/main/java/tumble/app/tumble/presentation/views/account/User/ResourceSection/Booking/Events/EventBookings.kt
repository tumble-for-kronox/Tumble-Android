package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.Settings.BackNav
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.SectionDivider
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator
import tumble.app.tumble.presentation.views.general.Info

@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventBookings(
    viewModel: ResourceViewModel = hiltViewModel(),
    navController: NavController
) {
    val completeUserEvent = viewModel.completeUserEvent.collectAsState()
    val eventBookingPageState = viewModel.eventBookingPageState.collectAsState()
    val scrollState = rememberScrollState()
    Scaffold (
        topBar = {
            BackNav(
                onClick = {navController.popBackStack()},
                label = stringResource(R.string.account),
                title = stringResource(R.string.events)
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.background)
                .padding(padding)
        ) {
            when (eventBookingPageState.value) {
                PageState.LOADING -> {
                    CustomProgressIndicator()
                }
                PageState.LOADED -> {
                    SectionDivider(
                        title = stringResource(id = R.string.registered),
                        image = Icons.Default.Tag,
                        content = {
                            completeUserEvent.value?.registeredEvents?.let { events ->
                                RegisteredEventsView(
                                    registeredEvents = events,
                                    onTapEventAction = { eventId, eventType ->
                                        onTapEventAction(viewModel, eventId, eventType)
                                    }
                                )
                            }

                        }
                    )
                    SectionDivider(
                        title = stringResource(id = R.string.unregistered),
                        image = Icons.Default.Tag,
                        content = {
                            completeUserEvent.value?.unregisteredEvents?.let { events ->
                                UnregisteredEventsView(
                                    unregisteredEvents = events,
                                    onTapEventAction = { eventId, eventType ->
                                        onTapEventAction(viewModel, eventId, eventType)
                                    }
                                )
                            }
                        }
                    )
                    SectionDivider(
                        title = stringResource(id = R.string.upcoming),
                        image = Icons.Default.Tag,
                        content = {
                            completeUserEvent.value?.upcomingEvents?.let {  events ->
                                UpcomingEventsView(upcomingEvents = events)
                            }
                        }
                    )
                }
                PageState.ERROR -> {
                    Info(
                        title = stringResource(id = R.string.error_something_wrong),
                        image = null,
                    )
                }
            }
            Spacer(Modifier.height(30.dp))
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getUserEventsForPage()
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