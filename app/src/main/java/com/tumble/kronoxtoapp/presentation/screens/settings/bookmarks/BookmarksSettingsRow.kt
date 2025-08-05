package com.tumble.kronoxtoapp.presentation.screens.settings.bookmarks

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.extensions.presentation.borderRadius

@Composable
fun BookmarkSettingsRow(
    schedule: Schedule,
    onDelete: (String) -> Unit,
    onToggle: (Boolean) -> Unit,
) {
    var isToggled by remember { mutableStateOf(schedule.toggled) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        label = "offsetX"
    )

    val maxSwipe = -175f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(IntrinsicSize.Min)
            .borderRadius(12.dp)
            .clipToBounds(),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.error)
                .padding(end = 15.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { onDelete(schedule.scheduleId) },
                modifier = Modifier
                    .size(40.dp)
                    .zIndex(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }

        Row(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.toInt(), 0) }
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 12.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 12.dp
                    )
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(maxSwipe, 0f)
                        },
                        onDragEnd = {
                            offsetX = if (offsetX < maxSwipe / 2) {
                                maxSwipe
                            } else {
                                0f
                            }
                        }
                    )
                }
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = schedule.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
                Text(
                    text = schedule.scheduleId,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                    fontSize = 14.sp
                )
            }

            Switch(
                checked = isToggled,
                onCheckedChange = { checked ->
                    isToggled = checked
                    onToggle(checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f),
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f)
                )
            )
        }
    }
}

