package tumble.app.tumble.presentation.screens.account.user.resources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RegisteredBooking(
    onClickResource: (NetworkResponse.KronoxUserBookingElement) -> Unit,
    state: State<PageState>,
    bookings: List<NetworkResponse.KronoxUserBookingElement>?,
    confirmBooking: (String, String) -> Unit,
    unBook: (String) -> Unit
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
                if(!bookings.isNullOrEmpty()){
                    bookings.forEach{ resource ->
                        val eventStart = resource.timeSlot.from?.convertToHoursAndMinutesISOString()
                        val eventEnd = resource.timeSlot.to?.convertToHoursAndMinutesISOString()
                        val noDate = stringResource(R.string.no_date)
                        ResourceCard(
                            eventStart = eventStart?: noDate,
                            eventEnd = eventEnd?: noDate,
                            title = stringResource(R.string.user_booked_resource),
                            location = resource.locationId,
                            date = resource.timeSlot.from?.formatDate()?: noDate,
                            onClick = { onClickResource(resource) }
                        )
                        if (resource.showConfirmButton){
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =  Arrangement.SpaceEvenly
                            ){
                                Button(
                                    onClick = {confirmBooking(resource.resourceId, resource.id)},
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(15.dp),
                                    ) {
                                    Text(
                                        text = stringResource(R.string.confirm),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Button(
                                    onClick = {unBook(resource.id)},
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(15.dp),) {
                                    Text(
                                        text = stringResource(R.string.cancel_booking),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }else{
                    Text(text = stringResource(R.string.no_booked_resource))
                }
            }
            PageState.ERROR -> {
                Text(text = stringResource(R.string.could_not_contact_server))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}