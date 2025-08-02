package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

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
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = locationId,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (bookingButtonState != BookingButtonState.BOOKED) {
                    onBook()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, disabledContainerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(17.dp),
            elevation = null,
            enabled = bookingButtonState != BookingButtonState.BOOKED,
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 8.dp)
        ) {
            when (bookingButtonState) {
                BookingButtonState.LOADING -> {
                    CustomProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
                BookingButtonState.BOOKED -> {
                    Text(
                        text = "Booked",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 0.sp
                    )
                }
                BookingButtonState.AVAILABLE -> {
                    Text(
                        text = "Book",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 0.sp
                    )
                }
            }
        }
    }
}