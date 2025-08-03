package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

// Option 1: Icon-only with color states (most compact)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPillIconOnly(
    state: NotificationState,
    title: String,
    onTap: () -> Unit
) {
    val isActive = state == NotificationState.SET

    FilledTonalButton(
        onClick = onTap,
        enabled = state != NotificationState.LOADING,
        modifier = Modifier
            .semantics {
                contentDescription = if (isActive)
                    "Remove $title notification"
                else
                    "Add $title notification"
            },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isActive)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        when (state) {
            NotificationState.SET -> {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            NotificationState.NOT_SET -> {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            NotificationState.LOADING -> {
                CustomProgressIndicator()
            }
        }
    }
}

// Option 2: Super short text (3-4 characters max)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPillShort(
    state: NotificationState,
    title: String, // "Event" or "Course"
    onTap: () -> Unit
) {
    val isActive = state == NotificationState.SET
    val displayText = when {
        title.startsWith("E") -> if (isActive) "E ✓" else "E"
        title.startsWith("C") -> if (isActive) "C ✓" else "C"
        else -> if (isActive) "ON" else "OFF"
    }

    FilledTonalButton(
        onClick = onTap,
        enabled = state != NotificationState.LOADING,
        modifier = Modifier.semantics {
            contentDescription = if (isActive)
                "Remove $title notification"
            else
                "Add $title notification"
        },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isActive)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (state) {
                NotificationState.SET, NotificationState.NOT_SET -> {
                    Icon(
                        imageVector = if (isActive) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
                    )
                }
                NotificationState.LOADING -> {
                    CustomProgressIndicator()
                }
            }
        }
    }
}

// Option 3: Toggle chip with minimal text
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPillChip(
    state: NotificationState,
    title: String,
    onTap: () -> Unit
) {
    val isActive = state == NotificationState.SET

    Button(
        onClick = onTap,
        enabled = state != NotificationState.LOADING,
        modifier = Modifier
            .semantics {
                contentDescription = if (isActive)
                    "$title notifications on, tap to turn off"
                else
                    "$title notifications off, tap to turn on"
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                Color.Transparent,
            contentColor = if (isActive)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (state) {
                NotificationState.SET, NotificationState.NOT_SET -> {
                    Icon(
                        imageVector = if (isActive) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = title.take(1), // Just "E" or "C"
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                NotificationState.LOADING -> {
                    CustomProgressIndicator()
                }
            }
        }
    }
}

// Option 4: Badge-style indicator
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPillBadge(
    state: NotificationState,
    title: String,
    onTap: () -> Unit
) {
    val isActive = state == NotificationState.SET

    FilledTonalButton(
        onClick = onTap,
        enabled = state != NotificationState.LOADING,
        modifier = Modifier.semantics {
            contentDescription = "$title notifications ${if (isActive) "enabled" else "disabled"}"
        },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
            contentColor = if (isActive)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
    ) {
        when (state) {
            NotificationState.SET -> {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            NotificationState.NOT_SET -> {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            NotificationState.LOADING -> {
                CustomProgressIndicator()
            }
        }
    }
}