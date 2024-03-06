package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import java.util.Locale

@Composable
fun CompactEventButtonLabel(event: Event, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (event.isSpecial) Color.Red.copy(alpha = 0.2f) else color.copy(alpha = 0.2f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        event.from.convertToHoursAndMinutesISOString()?.let { time ->
            Row(
                modifier = Modifier
                    .width(65.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(if (event.isSpecial) Color.Red else color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = time,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        }
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Event Course Name
            Row {
                Text(
                    text = event.course?.englishName ?: stringResource(R.string.unknown),
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                if (event.isSpecial) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "important_event",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Event Location
            Row {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "location",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = event.locations?.firstOrNull()?.locationId?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }
                        ?: stringResource(R.string.unknown),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
            }

            // Event Teachers
            Row {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                event.teachers?.firstOrNull()?.let { teacher ->
                    if (teacher.firstName!!.isNotEmpty() && teacher.lastName!!.isNotEmpty()) {
                        Text(
                            text = "${teacher.firstName} ${teacher.lastName}",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            fontSize = 15.sp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_teachers_listed),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            fontSize = 15.sp
                        )
                    }
                } ?: Text(
                    text = stringResource(R.string.no_teachers_listed),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
            }
        }
    }
}
