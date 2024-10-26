package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.components.sheets.SheetHeader
import tumble.app.tumble.presentation.views.bookmarks.EventDetails.DetailsBuilder


@Composable
fun ResourceDetailsSheet(
    booking: NetworkResponse.KronoxUserBookingElement,
    onClose: () -> Unit,
    onBookingRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        SheetHeader(title = stringResource(R.string.resource_details), onClose = onClose)
        Box {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colors.background)
            ) {

                DetailsBuilder(title = stringResource(R.string.location), image = Icons.Default.LocationOn) {
                    if (booking.locationId.isNotEmpty()) {
                        Text(
                            text = booking.locationId,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_location),
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
                DetailsBuilder(title = stringResource(R.string.timeslot), image = Icons.Default.Timelapse) {
                    Text(
                        text = "${
                            booking.timeSlot.from?.convertToHoursAndMinutesISOString().orEmpty()
                        } - ${booking.timeSlot.to?.convertToHoursAndMinutesISOString().orEmpty()}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                DetailsBuilder(title = stringResource(R.string.date), image = Icons.Default.CalendarMonth) {
                    Text(
                        text = booking.timeSlot.from?.formatDate().orEmpty(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                DetailsBuilder(title = stringResource(R.string.confirmation), image = Icons.Default.CheckCircleOutline) {
                    val bookingDate = booking.timeSlot.from?.formatDate()
                    val bookingConfirmationStart =
                        booking.confirmationOpen.convertToHoursAndMinutesISOString()
                    val bookingConfirmationEnd =
                        booking.confirmationClosed.convertToHoursAndMinutesISOString()
                    Text(
                        text = "$bookingDate, from $bookingConfirmationStart to $bookingConfirmationEnd"
                    )
                }

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (booking.showUnbookButton) {
            Button(
                onClick = { onBookingRemove() },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                elevation = null
            ) {

                Text(
                    text = stringResource(R.string.remove_booking),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
