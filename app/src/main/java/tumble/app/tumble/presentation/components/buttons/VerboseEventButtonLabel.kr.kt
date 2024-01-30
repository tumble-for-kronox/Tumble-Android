package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import tumble.app.tumble.presentation.extensions.borderRadius
import tumble.app.tumble.presentation.extensions.convertToHoursAndMinutesISOString
import tumble.app.tumble.presentation.extensions.toColor
import java.util.Locale

@Composable
fun VerboseEventButtonLabel(event: Event) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .background(if (event.isSpecial) Color.Red.copy(alpha = 0.2f) else MaterialTheme.colors.surface)
            .borderRadius(15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .height(160.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Course Name and Event Title
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = event.course?.englishName ?: "",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = event.title,
                    maxLines = 1,
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            // Teachers
            Row {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = event.teachers?.firstOrNull()?.let { teacher ->
                        if (teacher.firstName!!.isNotEmpty() && teacher.lastName!!.isNotEmpty()) {
                            "${teacher.firstName} ${teacher.lastName}"
                        } else {
                            stringResource(R.string.no_teachers_listed)
                        }
                    } ?: stringResource(R.string.no_teachers_listed),
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            // Location and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Location
                Row {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = event.locations?.firstOrNull()?.locationId?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                            ?: stringResource(R.string.unknown),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                // Time
                event.from.convertToHoursAndMinutesISOString()?.let { timeFrom ->
                    event.to.convertToHoursAndMinutesISOString()?.let { timeTo ->
                        Row {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(if (event.isSpecial) Color.Red else event.course?.color?.toColor() ?: Color.White)
                            )
                            Text(
                                text = "$timeFrom - $timeTo",
                                fontSize = 14.sp,
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
