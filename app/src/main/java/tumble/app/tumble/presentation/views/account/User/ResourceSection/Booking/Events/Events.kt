package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tumble.app.tumble.R
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
    UnregisteredEventsView(unregisteredEvents, onTapEventAction)
    RegisteredEventsView(registeredEvents, onTapEventAction)
    UpcomingEventsView(upcomingEvents)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisteredEventsView(
    registeredEvents: List<AvailableKronoxUserEvent>?,
    onTapEventAction: ((String, EventType) -> Unit)?
) {
    Column {
        registeredEvents?.let { events ->
            onTapEventAction?.let { action ->

                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    registeredEvents.forEach { event ->
                        EventCardButton(
                            event = event,
                            eventType = EventType.UNREGISTER,
                            onTap = { action(event.id!!, EventType.UNREGISTER) }
                        )
                    }
                }
            }
            if (events.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_registered_events),
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnregisteredEventsView(
    unregisteredEvents: List<AvailableKronoxUserEvent>?,
    onTapEventAction: ((String, EventType) -> Unit)?
) {
    Column {
        unregisteredEvents?.let { events ->
            onTapEventAction?.let { action ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    unregisteredEvents.forEach { event ->
                        EventCardButton(
                            event = event,
                            eventType = EventType.REGISTER,
                            onTap = { action(event.id!!, EventType.REGISTER) }
                        )
                    }
                }
            }
            if (events.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_unregistered_events),
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingEventsView(
    upcomingEvents: List<UpcomingKronoxUserEvent>?
) {
    Column {
        upcomingEvents?.let { events ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    upcomingEvents.forEach { event ->
                        UpcomingEventCardButton(event = event)
                    }
                }
            if (events.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_upcoming_events),
                    modifier = Modifier
                        .padding(top = 5.dp)
                )
            }
        }
    }
}
