package com.tumble.kronoxtoapp.extensions.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

fun Modifier.borderRadius(size: Dp) = clip(RoundedCornerShape(size))

fun Modifier.searchBox() = padding(10.dp)
    .background(Color.Gray.copy(alpha= 0.3F))
    .borderRadius(10.dp)
    .padding(top = 15.dp, bottom = 10.dp)
    .padding(horizontal = 15.dp)

fun Modifier.wiggle(): Modifier = composed {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val animationDelay = 1200 // in milliseconds
        isAnimating = true
        delay(animationDelay.toLong())
        isAnimating = false
    }

    val animatedRotation by animateFloatAsState(
        targetValue = if (isAnimating) -2f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )

    val animatedOffset by animateFloatAsState(
        targetValue = if (isAnimating) -15f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )

    this
        .graphicsLayer {
            rotationZ = animatedRotation
            translationX = animatedOffset.dp.toPx()
        }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}