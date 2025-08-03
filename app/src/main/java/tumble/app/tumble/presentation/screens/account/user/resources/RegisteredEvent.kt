package tumble.app.tumble.presentation.screens.account.user.resources

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
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RegisteredEvent(
    onClickEvent: (NetworkResponse.AvailableKronoxUserEvent) -> Unit,
    state: State<PageState>,
    registeredEvents: List<NetworkResponse.AvailableKronoxUserEvent>?
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when(state.value){
            PageState.LOADING -> {
                Row(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomProgressIndicator()
                }
            }
            PageState.LOADED -> {
                if(!registeredEvents.isNullOrEmpty()){
                    registeredEvents.forEach{ event ->
                        val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
                        val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
                        if (eventStart != null && eventEnd != null){
                            ResourceCard(
                                eventStart = eventStart,
                                eventEnd = eventEnd,
                                type = event.type,
                                title = event.title,
                                date = event.eventStart.formatDate()?: stringResource(R.string.no_date),
                                onClick = { onClickEvent(event) }
                            )
                        }
                    }
                }else{
                    Text(text = stringResource(R.string.no_registered_event_yet))
                }
            }
            PageState.ERROR -> {
                Text(text = stringResource(R.string.could_not_contact_server))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}