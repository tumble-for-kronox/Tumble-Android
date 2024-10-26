package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.extensions.presentation.isValidRegistrationDate

enum class EventType {
    REGISTER,
    UNREGISTER
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

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.background(MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp)).padding(16.dp).fillMaxWidth()
        ) {
            // Event Title
            Text(
                text = event.title ?: "stringResource(id = R.string.no_title)",
                fontSize = 17.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // Event Date and Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.CalendarToday),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                val eventDate = event.eventStart.formatDate()
                val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
                val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
                val eventDateText =
                    if (eventDate != null && eventStart != null && eventEnd != null) {
                        "$eventDate: $eventStart - $eventEnd"
                    } else {
                        stringResource(id = R.string.no_date)
                    }
                Text(
                    text = eventDateText,
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            // Signup Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Edit),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                val signupText = if (event.lastSignupDate.isValidRegistrationDate()) {
                    "${stringResource(id = R.string.available_until)} ${(event.lastSignupDate.formatDate()
                        ) ?: stringResource(id = R.string.no_date)
                    }"
                } else {
                    stringResource(id = R.string.signup_has_passed)
                }
                Text(
                    text = signupText,
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        if (event.lastSignupDate.isValidRegistrationDate()) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        event.id?.let { onTap() } },
                    modifier = Modifier
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Event),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (eventType == EventType.UNREGISTER) stringResource(id = R.string.unregister) else stringResource(id = R.string.register),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}