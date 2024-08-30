package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NetworkResponse.AvailableKronoxUserEvent
import tumble.app.tumble.domain.models.network.NetworkResponse.UpcomingKronoxUserEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Events(
    registeredEvents: List<AvailableKronoxUserEvent>? = null,
    unregisteredEvents: List<AvailableKronoxUserEvent>? = null,
    upcomingEvents: List<UpcomingKronoxUserEvent>? = null,
    onTapEventAction: ((String, EventType) -> Unit)? = null
) {
    unregisteredEventsView(unregisteredEvents, onTapEventAction)
    registeredEventsView(registeredEvents, onTapEventAction)
    upcomingEventsView(upcomingEvents)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun registeredEventsView(
    registeredEvents: List<NetworkResponse.AvailableKronoxUserEvent>?,
    onTapEventAction: ((String, EventType) -> Unit)?
) {
    Column {
        registeredEvents?.let { events ->
            onTapEventAction?.let { action ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    events.forEach { event ->
                        EventCardButton(
                            event = event,
                            eventType = EventType.UNREGISTER,
                            onTap = { action(event.eventId!!, EventType.UNREGISTER) }
                        )
                    }
                }
            }
            if (events.isEmpty()) {
                Text(
                    text = "stringResource(id = R.string.no_registered_events)",
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun unregisteredEventsView(
    unregisteredEvents: List<NetworkResponse.AvailableKronoxUserEvent>?,
    onTapEventAction: ((String, EventType) -> Unit)?
) {
    Column {
        unregisteredEvents?.let { events ->
            onTapEventAction?.let { action ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    events.forEach { event ->
                        EventCardButton(
                            event = event,
                            eventType = EventType.REGISTER,
                            onTap = { action(event.eventId!!, EventType.REGISTER) }
                        )
                    }
                }
            }
            if (events.isEmpty()) {
                Text(
                    text = "stringResource(id = R.string.no_unregistered_events)",
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun upcomingEventsView(
    upcomingEvents: List<NetworkResponse.UpcomingKronoxUserEvent>?
) {
    Column {
        upcomingEvents?.let { events ->
            Column(modifier = Modifier.fillMaxWidth()) {
                events.forEach { event ->
                    UpcomingEventCardButton(event = event)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            if (events.isEmpty()) {
                Text(
                    text = "stringResource(id = R.string.no_upcoming_events)",
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}