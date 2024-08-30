package tumble.app.tumble.presentation.views.account.User.ResourceSection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RegisteredEvent(
    onClickEvent: (NetworkResponse.AvailableKronoxUserEvent) -> Unit,
    state: State<PageState>,
    registeredEvents: List<NetworkResponse.AvailableKronoxUserEvent>?
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        when(state.value){
            PageState.LOADING -> {
                CustomProgressIndicator()
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
                                date = event.eventStart?:"(no date)",
                                onClick = { onClickEvent(event) }

                            )
                        }

                    }
                }else{
                    Text(
                        text = "No registed event yet",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            PageState.ERROR -> {
                Text(text = "Could not contact the server, try again later",
                    modifier = Modifier.padding(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


}