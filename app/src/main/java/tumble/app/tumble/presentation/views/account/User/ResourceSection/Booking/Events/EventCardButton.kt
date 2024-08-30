package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.isValidRegistrationDate
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import tumble.app.tumble.utils.isoDateFormatterDate

enum class EventType(val icon: String) {
    REGISTER("check_circle"),
    UNREGISTER("cancel")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCardButton(
    event: NetworkResponse.AvailableKronoxUserEvent,
    eventType: EventType,
    onTap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(15.dp)) // Replace with your theme colors
            .padding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Event Title
                Text(
                    text = event.title ?: "stringResource(id = R.string.no_title)",
                    fontSize = 17.sp,
                    color = MaterialTheme.colors.onSurface, // Replace with your theme colors
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Event Date and Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.CalendarToday),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Replace with your theme colors
                    )
                    val eventDate = event.eventStart.toLocalDateTime()
                    val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
                    val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
                    val eventDateText = if (eventDate != null && eventStart != null && eventEnd != null) {
                        String.format("stringResource(id = R.string.event_time_format)", eventDate, eventStart, eventEnd)
                    } else {
                        "stringResource(id = R.string.no_date_at_this_time)"
                    }
                    Text(
                        text = eventDateText,
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Replace with your theme colors
                    )
                }
                // Signup Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Edit),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Replace with your theme colors
                    )
                    val signupText = if (event.lastSignupDate.isValidRegistrationDate()) {
                        "${"stringResource(id = R.string.available_until)"} ${isoDateFormatterDate.parse(event.lastSignupDate) ?: "stringResource(id = R.string.no_date_set)"}"
                    } else {
                        "stringResource(id = R.string.signup_has_passed)"
                    }
                    Text(
                        text = signupText,
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f) // Replace with your theme colors
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        if (event.lastSignupDate.isValidRegistrationDate()) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { event.eventId?.let { onTap() } },
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp)) // Replace with your theme colors
                        .padding(10.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Event),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary// Replace with your theme colors
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (eventType == EventType.UNREGISTER) "stringResource(id = R.string.unregister)" else "stringResource(id = R.string.register)",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onPrimary// Replace with your theme colors
                    )
                }
            }
        }
    }
}