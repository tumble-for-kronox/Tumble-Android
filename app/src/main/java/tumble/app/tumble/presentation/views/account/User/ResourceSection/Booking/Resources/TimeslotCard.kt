package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TimeslotCard(
    onBook: () -> Unit,
    locationId: String,
    bookingButtonState: BookingButtonState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .height(70.dp)
            .padding(10.dp)
            .background(MaterialTheme.colors.surface)
    ) {
        Text(
            text = locationId,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (bookingButtonState != BookingButtonState.BOOKED) {
                    onBook()
                }
            },
            enabled = bookingButtonState != BookingButtonState.BOOKED,
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(7.5.dp)
        ) {
            when (bookingButtonState) {
                BookingButtonState.LOADING -> {
                    CustomProgressIndicator(color = MaterialTheme.colors.onPrimary)
                }
                BookingButtonState.BOOKED -> {
                    Text(
                        text = "Booked",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                BookingButtonState.AVAILABLE -> {
                    Text(
                        text = "Book",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}