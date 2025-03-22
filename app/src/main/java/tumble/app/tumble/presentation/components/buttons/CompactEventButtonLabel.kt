package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun CompactEventButtonLabel(event: Event,
                            color: Color,
                            onEventSelection: (Event) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (event.isSpecial) Color.Red.copy(alpha = 0.2f) else color.copy(alpha = 0.2f),
                RoundedCornerShape(15.dp)
            ).clickable { onEventSelection(event) }
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        event.from.convertToHoursAndMinutesISOString()?.let { timeStart ->
            event.to.convertToHoursAndMinutesISOString()?.let { timeEnd ->
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = timeStart,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = MaterialTheme.colors.onSurface
                )
                Text(
                    text = timeEnd,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        }
        }
        Column(
            modifier = Modifier.padding(10.dp).padding(start = 5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Event Name
            Row {
                Text(
                    text = event.title,
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
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            // Event Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "location",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(end = 5.dp).size(15.dp)
                )
                Text(
                    text = event.locations?.firstOrNull()?.locationId?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }
                        ?: stringResource(R.string.unknown),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.weight(1f))
            // Event Teachers
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(end = 5.dp).size(15.dp)
                )
                event.teachers?.firstOrNull()?.let { teacher ->
                    if (teacher.firstName!!.isNotEmpty() && teacher.lastName!!.isNotEmpty()) {
                        Text(
                            text = "${teacher.firstName} ${teacher.lastName}",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_teachers_listed),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } ?: Text(
                    text = stringResource(R.string.no_teachers_listed),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
