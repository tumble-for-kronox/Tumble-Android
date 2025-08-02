package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Events


import android.os.Build
import androidx.annotation.RequiresApi
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
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.InformationView

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
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                .padding(15.dp)
        ) {
            Text(
                text = event.title,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )

            val eventDate = event.eventStart.formatDate()
            val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
            val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
            val eventDateText = if (eventDate != null && eventStart != null && eventEnd != null) {
                "$eventDate, ${stringResource(id = R.string.from)} $eventStart ${stringResource(id = R.string.to)} $eventEnd"
            } else {
                stringResource(id = R.string.no_date)
            }
            InformationView(Icons.Default.CalendarMonth, eventDateText)

            val signupText = if (event.lastSignupDate.isValidRegistrationDate()) {
                "${stringResource(id = R.string.available_until)} ${(event.lastSignupDate.formatDate()
                        ) ?: stringResource(id = R.string.no_date)
                }"
            } else {
                stringResource(id = R.string.signup_has_passed)
            }
            InformationView(Icons.Default.Edit, signupText)
        }
        if (event.lastSignupDate.isValidRegistrationDate()) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            ) {
                Button(
                    onClick = {
                        event.id?.let { onTap() } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.CheckCircle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (eventType == EventType.UNREGISTER) stringResource(id = R.string.unregister) else stringResource(id = R.string.register),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 0.sp
                    )
                }
            }
        }
    }
}