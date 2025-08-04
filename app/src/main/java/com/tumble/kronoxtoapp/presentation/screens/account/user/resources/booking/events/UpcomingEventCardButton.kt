package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.extensions.presentation.convertToHoursAndMinutesISOString
import com.tumble.kronoxtoapp.extensions.presentation.formatDate
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.DetailItem

@Composable
fun UpcomingEventCardButton(
    event: NetworkResponse.UpcomingKronoxUserEvent
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
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

            // First Signup Date
            DetailItem(
                icon = Icons.Default.Edit,
                text = "${stringResource(id = R.string.available_at)}: ${
                    event.firstSignupDate.formatDate()
                        ?: stringResource(id = R.string.no_date)
                }"
            )
        }
    }
}
