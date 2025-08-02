package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString
import java.util.Locale

@Composable
fun CompactEventButtonLabel(
    event: Event,
    color: Color,
    onEventSelection: (Event) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .clickable { onEventSelection(event) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Event Title
            Text(
                text = event.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Location and Teacher Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Location
                    DetailItem(
                        icon = Icons.Outlined.LocationOn,
                        text = event.locations?.firstOrNull()?.locationId?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                        } ?: stringResource(R.string.unknown)
                    )

                    // Teacher
                    DetailItem(
                        icon = Icons.Outlined.Person,
                        text = getTeacherDisplayName(event) ?: stringResource(R.string.no_teachers_listed)
                    )
                }

                // Time Display
                TimeRangeChip(
                    startTime = event.from.convertToHoursAndMinutesISOString(),
                    endTime = event.to.convertToHoursAndMinutesISOString(),
                    isSpecial = event.isSpecial,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun TimeRangeChip(
    startTime: String?,
    endTime: String?,
    isSpecial: Boolean,
    color: Color
) {
    val backgroundColor = if (isSpecial) {
        MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
    } else {
        color.copy(alpha = 0.12f)
    }

    val textColor = if (isSpecial) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.wrapContentWidth()
    ) {
        if (startTime != null && endTime != null) {
            Text(
                text = "$startTime - $endTime",
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        } else {
            Text(
                text = "Time TBA",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getTeacherDisplayName(event: Event): String? {
    val teacher = event.teachers?.firstOrNull() ?: return null

    val fullName = listOfNotNull(teacher.firstName, teacher.lastName).joinToString(" ")
    if (fullName.isBlank()) return null

    val nameParts = fullName.trim().split("\\s+".toRegex())
    val lastName = nameParts.lastOrNull() ?: return fullName

    val abbreviated = when {
        nameParts.size >= 3 -> {
            // if three or more parts abbreviate all except the last one
            val shortened = nameParts.dropLast(1).joinToString(" ") { part ->
                part.first().uppercaseChar() + "."
            }
            "$shortened $lastName"
        }
        nameParts.size == 2 -> {
            val (first, last) = nameParts
            "$first $last"
        }
        else -> fullName
    }

    // fallback if even the abbreviated name is still longer than 20 chars
    return if (abbreviated.length > 20) {
        val shortened = nameParts.dropLast(1).joinToString(" ") { part ->
            part.first().uppercaseChar() + "."
        }
        "$shortened $lastName"
    } else abbreviated
}
