package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.formatDate

@Composable
fun EventDetailsBody(event: Event){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        DetailsBuilder(title = "Course", image = Icons.Default.Book){
            Text(
                text = event.course?.englishName.orEmpty(),
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        DetailsBuilder(title = "Teachers", image = Icons.Default.Person2) {
            if(event.teachers != null){
                if (event.teachers?.firstOrNull()?.firstName?.isNotEmpty() == true &&
                    event.teachers?.firstOrNull()?.lastName?.isNotEmpty() == true
                ){
                    event.teachers?.forEach { teacher ->
                        Text(
                            text = "${teacher.firstName} ${teacher.lastName}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                } else{
                    Text(
                        text = "No teachers listed at this time",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            } else{
                Text(
                    text = "No teachers listed at this time",
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
        DetailsBuilder(title = "Date", image = Icons.Default.CalendarMonth) {
            Text(
                text = event.from.formatDate().orEmpty(),
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        DetailsBuilder(title = "Time", image = Icons.Default.AccessTime) {
            Text(
                text = "${event.from.convertToHoursAndMinutesISOString().orEmpty()} - ${event.to.convertToHoursAndMinutesISOString().orEmpty()}",
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        DetailsBuilder(title = "Location", image = Icons.Default.LocationOn) {
            if (event.locations?.isNotEmpty() == true){
                event.locations?.forEach { location ->
                    Text(
                        text = location.locationId.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }else {
                Text(
                    text = "No location listed at this time",
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}