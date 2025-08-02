package tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.InformationView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingEventCardButton(
    event: NetworkResponse.UpcomingKronoxUserEvent
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth().padding(15.dp)
        ) {
            Text(
                text = event.title,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )
            val eventDate = event.eventStart.formatDate()
            val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
            val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
            val eventDateText = if (eventDate != null && eventStart != null && eventEnd != null) {
                "$eventDate, ${stringResource(id = R.string.from)} $eventStart ${stringResource(id = R.string.to)} $eventEnd"
            } else {
                stringResource(id = R.string.no_date)
            }
            InformationView(Icons.Default.CalendarMonth, eventDateText)
            InformationView(Icons.Default.Edit, "${stringResource(id = R.string.available_at)}: ${event.firstSignupDate.formatDate() ?: stringResource(id = R.string.no_date)}")
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}