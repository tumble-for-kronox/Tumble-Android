package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.extensions.presentation.convertToHoursAndMinutesISOString
import com.tumble.kronoxtoapp.extensions.presentation.formatDate
import com.tumble.kronoxtoapp.extensions.presentation.isValidRegistrationDate

enum class EventType {
    REGISTER,
    UNREGISTER
}

@Composable
fun EventCardButton(
    event: NetworkResponse.AvailableKronoxUserEvent,
    eventType: EventType,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isRegistrationOpen = event.lastSignupDate.isValidRegistrationDate()
    val scale by animateFloatAsState(
        targetValue = if (isExpanded) 1.02f else 1f,
        animationSpec = tween(200),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                onClick = { isExpanded = !isExpanded }
            )
            .semantics {
                contentDescription = "Event: ${event.title}"
                if (isRegistrationOpen) {
                    stateDescription = "Registration available"
                } else {
                    stateDescription = "Registration closed"
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Title and Status
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EventStatusBadge(
                        eventType = eventType
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Info (Always Visible)
            QuickEventInfo(event = event)

            // Expanded Details
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detailed Info
                    DetailedEventInfo(event = event)

                    if (isRegistrationOpen) {
                        Spacer(modifier = Modifier.height(20.dp))

                        // Action Button
                        EventActionButton(
                            eventType = eventType,
                            onTap = onTap,
                            isEnabled = event.id != null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventStatusBadge(
    eventType: EventType
) {
    val (backgroundColor, contentColor, text, icon) = when {
        eventType == EventType.UNREGISTER -> {
            Tuple4(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.onSurfaceVariant,
                "Registered",
                Icons.Default.CheckCircle
            )
        }
        else -> {
            Tuple4(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.onSecondary,
                "Available",//stringResource(id = R.string.available),
                Icons.Outlined.EventAvailable
            )
        }
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = Modifier.semantics {
            contentDescription = text
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = contentColor
            )
        }
    }
}

@Composable
private fun QuickEventInfo(event: NetworkResponse.AvailableKronoxUserEvent) {
    val eventDate = event.eventStart.formatDate()
    val eventStart = event.eventStart.convertToHoursAndMinutesISOString()
    val eventEnd = event.eventEnd.convertToHoursAndMinutesISOString()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date
        InfoChip(
            icon = Icons.Outlined.Today,
            text = eventDate ?: stringResource(id = R.string.no_date),
            modifier = Modifier.weight(1f)
        )

        // Time
        if (eventStart != null && eventEnd != null) {
            InfoChip(
                icon = Icons.Outlined.Schedule,
                text = "$eventStart - $eventEnd",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DetailedEventInfo(event: NetworkResponse.AvailableKronoxUserEvent) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val signupText = if (event.lastSignupDate.isValidRegistrationDate()) {
            "${stringResource(id = R.string.available_until)} ${
                event.lastSignupDate.formatDate() ?: stringResource(id = R.string.no_date)
            }"
        } else {
            stringResource(id = R.string.signup_has_passed)
        }

        DetailRow(
            icon = Icons.Outlined.EditCalendar,
            title = "Registration deadline", // stringResource(id = R.string.registration_deadline),
            content = signupText
        )

        // Add more details as needed
        event.id?.let { id ->
            DetailRow(
                icon = Icons.Outlined.Tag,
                title = "Event ID",
                content = id
            )
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    title: String,
    content: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun EventActionButton(
    eventType: EventType,
    onTap: () -> Unit,
    isEnabled: Boolean
) {
    val (backgroundColor, contentColor, text, icon) = when (eventType) {
        EventType.UNREGISTER -> {
            Tuple4(
                MaterialTheme.colorScheme.error,
                MaterialTheme.colorScheme.onError,
                stringResource(id = R.string.unregister),
                Icons.Default.PersonRemove
            )
        }
        EventType.REGISTER -> {
            Tuple4(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary,
                stringResource(id = R.string.register),
                Icons.Default.PersonAdd
            )
        }
    }

    Button(
        onClick = onTap,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

// Helper class for multiple return values
private data class Tuple4<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)