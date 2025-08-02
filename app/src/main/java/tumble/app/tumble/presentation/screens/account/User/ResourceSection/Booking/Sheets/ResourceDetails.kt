package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.screens.bookmarks.EventDetails.DetailsBuilder
import tumble.app.tumble.presentation.screens.navigation.AppBarState


@Composable
fun ResourceDetailsSheet(
    booking: NetworkResponse.KronoxUserBookingElement,
    onClose: () -> Unit,
    onBookingRemove: () -> Unit,
    setTopNavState: (AppBarState) -> Unit

) {
    val title = stringResource(R.string.resource_details)

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = title,
                actions = {
                    CloseCoverButton {
                        onClose()
                    }
                }
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background).padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        DetailsBuilder(title = stringResource(R.string.location), image = Icons.Default.LocationOn) {
            if (booking.locationId.isNotEmpty()) {
                Text(
                    text = booking.locationId,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = stringResource(R.string.no_location),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        DetailsBuilder(title = stringResource(R.string.timeslot), image = Icons.Default.Timelapse) {
            Text(
                text = "${
                    booking.timeSlot.from?.convertToHoursAndMinutesISOString().orEmpty()
                } - ${booking.timeSlot.to?.convertToHoursAndMinutesISOString().orEmpty()}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        DetailsBuilder(title = stringResource(R.string.date), image = Icons.Default.CalendarMonth) {
            Text(
                text = booking.timeSlot.from?.formatDate().orEmpty(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        DetailsBuilder(title = stringResource(R.string.confirmation), image = Icons.Default.CheckCircleOutline) {
            val bookingDate = booking.timeSlot.from?.formatDate()
            val bookingConfirmationStart =
                booking.confirmationOpen?.convertToHoursAndMinutesISOString()
            val bookingConfirmationEnd =
                booking.confirmationClosed?.convertToHoursAndMinutesISOString()
            Text(
                text = "$bookingDate, from $bookingConfirmationStart to $bookingConfirmationEnd"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (booking.showUnbookButton) {
            Button(
                onClick = { onBookingRemove() },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth(),
                elevation = null
            ) {
                Text(
                    text = stringResource(R.string.remove_booking),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    letterSpacing = 0.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }
    }
}
