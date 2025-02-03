package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.borderRadius
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import tumble.app.tumble.extensions.presentation.toColor
import java.util.Locale

@Composable
fun VerboseEventButtonLabel(event: Event) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .borderRadius(15.dp)
            .background(MaterialTheme.colors.surface)
            .background(if (event.isSpecial) Color.Red.copy(alpha = 0.2f) else MaterialTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Course Name and Event Title
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = event.title,
                    maxLines = 2,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = event.course?.englishName ?: "",
                    maxLines = 1,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(Modifier.height(20.dp))
            // Teachers
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 5.dp).size(15.dp)
                )
                event.teachers?.firstOrNull()?.let { teacher ->
                    if (teacher.firstName!!.isNotEmpty() && teacher.lastName!!.isNotEmpty()) {
                        Text(
                            text = "${teacher.firstName} ${teacher.lastName}",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_teachers_listed),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } ?: Text(
                    text = stringResource(R.string.no_teachers_listed),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Location and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "location",
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(end = 5.dp).size(15.dp)
                    )
                    Text(
                        text = event.locations?.firstOrNull()?.locationId?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                            ?: stringResource(R.string.unknown),
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Time
                event.from.convertToHoursAndMinutesISOString()?.let { timeFrom ->
                    event.to.convertToHoursAndMinutesISOString()?.let { timeTo ->
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (event.isSpecial) Color.Red else event.course?.color?.toColor() ?: Color.White)
                            )
                            Text(
                                text = timeFrom,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = timeTo,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
