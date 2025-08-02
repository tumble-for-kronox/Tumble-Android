package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Events


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.extensions.presentation.isValidRegistrationDate
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.DetailItem

enum class EventType {
    REGISTER,
    UNREGISTER
}

@Composable
fun EventCardButton(
    event: NetworkResponse.AvailableKronoxUserEvent,
    eventType: EventType,
    onTap: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = event.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                lineHeight = 22.sp
            )

            // Date
            val eventDate = event.eventStart.formatDate()
            val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
            val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
            val eventDateText = if (eventDate != null && eventStart != null && eventEnd != null) {
                "$eventDate, ${stringResource(id = R.string.from)} $eventStart ${stringResource(id = R.string.to)} $eventEnd"
            } else {
                stringResource(id = R.string.no_date)
            }
            DetailItem(icon = Icons.Default.CalendarMonth, text = eventDateText)

            // Signup Info
            val signupText = if (event.lastSignupDate.isValidRegistrationDate()) {
                "${stringResource(id = R.string.available_until)} ${
                    event.lastSignupDate.formatDate()
                        ?: stringResource(id = R.string.no_date)
                }"
            } else {
                stringResource(id = R.string.signup_has_passed)
            }
            DetailItem(icon = Icons.Default.Edit, text = signupText)

            // Button
            if (event.lastSignupDate.isValidRegistrationDate()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { event.id?.let { onTap() } },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (eventType == EventType.UNREGISTER)
                                stringResource(id = R.string.unregister)
                            else
                                stringResource(id = R.string.register),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
