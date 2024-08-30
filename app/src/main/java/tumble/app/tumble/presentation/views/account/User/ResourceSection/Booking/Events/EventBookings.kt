package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.background)
        ) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                CloseCoverButton {
                    navController.popBackStack()
                }
            }
            when (eventBookingPageState.value) {
                PageState.LOADING -> {
                    CustomProgressIndicator()
                }
                PageState.LOADED -> {
                    SectionDivider(
                        title = "stringResource(id = R.string.registered)",
                        image = Icons.Default.Tag,
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
                                    text = "stringResource(id = R.string.no_registered_events)",
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                    SectionDivider(
                        title = "stringResource(id = R.string.unregistered)",
                        image = Icons.Default.Tag,
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
                                    text = "stringResource(id = R.string.no_unregistered_events)",
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                    SectionDivider(
                        title = "stringResource(id = R.string.upcoming)",
                        image = Icons.Default.Tag,
                        content = {
                            if (completeUserEvent.value?.upcomingEvents != null) {
                                Events(upcomingEvents = completeUserEvent.value?.upcomingEvents)
                            } else {
                                Text(
                                    text = "stringResource(id = R.string.no_upcoming_events)",
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                }
                PageState.ERROR -> {
                    Info(
                        title = "stringResource(id = R.string.server_error)",
                        image = null,
                    )
                }
            }
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