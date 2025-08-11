package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events

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
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.SectionDivider
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountEventsState
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountEventsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EventBookings(
    viewModel: AccountEventsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.events)
    val backTitle = stringResource(R.string.account)
    val scrollState = rememberScrollState()

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
            when (viewModel.state) {
                is AccountEventsState.Loading -> {
                    CustomProgressIndicator()
                }

                is AccountEventsState.Error -> {
                    val errorMessage = (viewModel.state as AccountEventsState.Error).message
                    Info(errorMessage)
                }

                is AccountEventsState.Loaded -> {
                    val events = (viewModel.state as AccountEventsState.Loaded).events
                    SectionDivider(
                        title = stringResource(R.string.registered),
                        image = Icons.Default.PersonAddAlt1,
                        content = {
                            if (events.registeredEvents != null) {
                                RegisteredEventsView(
                                    registeredEvents = events.registeredEvents,
                                    onTapEventAction = { eventId, eventType ->
                                        viewModel.bookingAction(eventId, eventType)
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
                            if (events.unregisteredEvents != null) {
                                UnregisteredEventsView(
                                    unregisteredEvents = events.unregisteredEvents,
                                    onTapEventAction = { eventId, eventType ->
                                        viewModel.bookingAction(eventId, eventType)
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
                            if (events.upcomingEvents != null) {
                                UpcomingEventsView(upcomingEvents = events.upcomingEvents)
                            } else {
                                Text(
                                    text = stringResource(R.string.no_upcoming_events),
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllEvents()
    }
}