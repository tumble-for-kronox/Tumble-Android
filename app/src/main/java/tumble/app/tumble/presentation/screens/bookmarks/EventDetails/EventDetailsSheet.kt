package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Palette
import io.mhssn.colorpicker.ColorPicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import io.mhssn.colorpicker.ext.toHex
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.toColor
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.screens.navigation.AppBarState

@Composable
fun EventDetailsSheet(
    event: Event,
    setTopNavState: (AppBarState) -> Unit,
    onClose: () -> Unit = {},
    onColorChanged: (String, String) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }
    var selectedColor by remember {
        mutableStateOf(event.course?.color?.toColor() ?: Color.Gray)
    }

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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EventHeaderCard(event = event, eventColor = selectedColor)

        Text(
            text = "Event Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Course
        event.course?.let { course ->
            DetailCard(
                icon = Icons.Default.School,
                title = "Course",
                content = {
                    Text(
                        text = course.englishName ?: "Course name not available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }

        // Teachers
        event.teachers?.let { teachers ->
            if (teachers.isNotEmpty()) {
                DetailCard(
                    icon = Icons.Default.Person,
                    title = if (teachers.size == 1) "Teacher" else "Teachers",
                    content = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            teachers.forEach { teacher ->
                                Text(
                                    text = "${teacher.firstName} ${teacher.lastName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                )
            }
        }

        // Date
        DetailCard(
            icon = Icons.Default.CalendarMonth,
            title = "Date",
            content = {
                Text(
                    text = event.from.substringBefore("T"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        )

        // Time slot
        DetailCard(
            icon = Icons.Default.Schedule,
            title = "Time",
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.from.substringAfter("T").substringBefore("+").substring(0, 5),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " - ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = event.to.substringAfter("T").substringBefore("+").substring(0, 5),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )

        // Locations
        event.locations?.let { locations ->
            if (locations.isNotEmpty()) {
                DetailCard(
                    icon = Icons.Default.LocationOn,
                    title = if (locations.size == 1) "Location" else "Locations",
                    content = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            locations.forEach { location ->
                                location.locationId?.let { locationId ->
                                    Text(
                                        text = locationId,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (location.maxSeats > 0) {
                                        Text(
                                            text = "Capacity: ${location.maxSeats} seats",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        // Special event indicator
        if (event.isSpecial) {
            DetailCard(
                icon = Icons.Default.Star,
                title = "Special Event",
                content = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = "This is a special event",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Color picker button
        ActionButtonsSection(
            onChangeColor = { showColorPicker = true }
        )
    }

    // Enhanced Color Picker Dialog
    if (showColorPicker) {
        ModernColorPickerDialog(
            currentColor = selectedColor,
            onColorSelected = { color ->
                selectedColor = color
                event.course?.courseId?.let { courseId ->
                    val hexColor = color.toHex()
                    val rgbOnly = "#${hexColor.takeLast(6)}"
                    onColorChanged(rgbOnly, courseId)
                }
                showColorPicker = false
            },
            onDismiss = { showColorPicker = false }
        )
    }
}

@Composable
private fun EventHeaderCard(
    event: Event,
    eventColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = eventColor,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.course?.englishName ?: "Course",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailCard(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon container
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}

@Composable
private fun ActionButtonsSection(
    onChangeColor: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onChangeColor,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Change Color",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ModernColorPickerDialog(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(currentColor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Choose Event Color",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Color picker
                ColorPicker(
                    onPickedColor = { color ->
                        selectedColor = color
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color preview card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Color preview circle
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = selectedColor,
                            shadowElevation = 2.dp
                        ) {}

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Preview",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatColorHex(selectedColor),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onColorSelected(selectedColor)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Select Color",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}

// Helper function to format color hex value
private fun formatColorHex(color: Color): String {
    return "#${color.toArgb().toUInt().toString(16).uppercase().takeLast(6)}"
}