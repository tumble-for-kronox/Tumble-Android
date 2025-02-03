package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.views.bookmarks.EventDetails.DetailsBuilder
import tumble.app.tumble.presentation.views.navigation.AppBarState

@Composable
fun EventDetailsSheet(
    event: NetworkResponse.AvailableKronoxUserEvent,
    onClose: () -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    val title = stringResource(R.string.event_details)
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
            .background(MaterialTheme.colors.background)
            .padding(15.dp)
    ) {
        DetailsBuilder(title = stringResource(R.string.title), image = Icons.Default.Title) {

                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )

        }
        DetailsBuilder(title = stringResource(R.string.type), image = Icons.Default.Info) {
            Text(
                text = event.type,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }

        DetailsBuilder(title = stringResource(R.string.date), image = Icons.Default.CalendarMonth) {
            val eventDate = event.eventStart.formatDate()
            val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
            val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()
            Text(
                text = "$eventDate, from $eventStart to $eventEnd"
                ,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }

        DetailsBuilder(title = stringResource(R.string.available_until), image = Icons.Default.Timelapse) {
            Text(
                text = event.lastSignupDate.formatDate().orEmpty()
                ,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}