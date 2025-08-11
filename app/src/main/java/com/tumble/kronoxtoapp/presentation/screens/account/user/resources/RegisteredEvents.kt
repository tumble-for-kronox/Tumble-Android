package com.tumble.kronoxtoapp.presentation.screens.account.user.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.other.extensions.presentation.convertToHoursAndMinutesISOString
import com.tumble.kronoxtoapp.other.extensions.presentation.formatDate
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountDataState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RegisteredEvents(
    onClickEvent: (NetworkResponse.AvailableKronoxUserEvent) -> Unit,
    eventsState: AccountDataState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (eventsState) {
            is AccountDataState.Idle,
            is AccountDataState.Loading -> {
                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomProgressIndicator()
                }
            }

            is AccountDataState.EventsLoaded -> {
                val registeredEvents = eventsState.events.registeredEvents
                registeredEvents?.let { events ->
                    if (events.isNotEmpty()) {
                        registeredEvents.forEach { event ->
                            val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
                            val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()

                            if (eventStart != null && eventEnd != null) {
                                ResourceCard(
                                    eventStart = eventStart,
                                    eventEnd = eventEnd,
                                    type = event.type,
                                    title = event.title,
                                    date = event.eventStart.formatDate() ?: stringResource(R.string.no_date),
                                    onClick = { onClickEvent(event) }
                                )
                            }
                        }
                    } else {
                        Text(text = stringResource(R.string.no_registered_event_yet))
                    }
                } ?: Text(text = stringResource(R.string.no_registered_event_yet))
            }

            is AccountDataState.Error -> {
                Text(text = stringResource(R.string.could_not_contact_server))
            }

            else -> {
                Text(text = stringResource(R.string.could_not_contact_server))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}