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
fun RegisteredBooking(
    onClickResource: (NetworkResponse.KronoxUserBookingElement) -> Unit,
    state: State<PageState>,
    bookings: List<NetworkResponse.KronoxUserBookingElement>?
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
                if(!bookings.isNullOrEmpty()){
                    bookings.forEach{ resource ->
                        ResourceCard(
                            eventStart = resource.timeSlot.from?.convertToHoursAndMinutesISOString()?:"(no date)",
                            eventEnd = resource.timeSlot.to?.convertToHoursAndMinutesISOString()?:"(no date)",
                            title = "Booked resource",
                            location = resource.locationID,
                            date = resource.timeSlot.from?: "(no date)",
                            onClick = { onClickResource(resource) }
                        )

                    }
                }else{
                    Text(text = "no booking resource yet",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            PageState.ERROR -> {
                Text(
                    text = "Could not contat the server, try again later",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}