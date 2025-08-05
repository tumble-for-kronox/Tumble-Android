package com.tumble.kronoxtoapp.presentation.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.other.extensions.presentation.noRippleClickable


@Composable
fun BackButton(label: String = "", onClick: () -> Unit) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = label,
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(
                start = 10.dp,
                top = 10.dp,
                end = 24.dp,
                bottom = 10.dp
            ),
        tint = MaterialTheme.colorScheme.primary
    )
}