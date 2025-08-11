package com.tumble.kronoxtoapp.presentation.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.domain.enums.ButtonState
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun BookmarkButton(
    onToggleBookmark: () -> Unit,
    buttonState: ButtonState,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggleBookmark,
        enabled = buttonState != ButtonState.Disabled && buttonState != ButtonState.Loading,
        modifier = modifier
    ) {
        when (buttonState) {
            ButtonState.Loading -> {
                CustomProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
            ButtonState.Disabled -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkAdd,
                    contentDescription = "Bookmark disabled",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    modifier = Modifier.size(24.dp)
                )
            }
            ButtonState.Saved -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkRemove,
                    contentDescription = "Remove bookmark",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            ButtonState.NotSaved -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkAdd,
                    contentDescription = "Add bookmark",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}