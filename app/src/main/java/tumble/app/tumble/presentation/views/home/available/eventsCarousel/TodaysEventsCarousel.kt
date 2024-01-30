package tumble.app.tumble.presentation.views.home.available.eventsCarousel

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.presentation.models.WeekEventCardModel
import kotlin.math.abs

@Composable
fun TodaysEventsCarousel(
    swipedCards: MutableState<Int>,
    weekEventCards: MutableList<WeekEventCardModel>
) {
    val showWiggleAnimation = remember {
        mutableStateOf(true)
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxSize()) {
        if (weekEventCards.isEmpty()) {
            Text(
                "No events for today", // Replace with string resource for localization
                fontSize = 18.sp,
                color = Color.Gray // Replace with your theme's onBackground color
            )
        } else {
            weekEventCards.asReversed().forEachIndexed { index, eventCard ->
                CarouselCard(
                    event = eventCard.event,
                    index = index,
                    eventsForToday = weekEventCards,
                    swipedCards = swipedCards.value,
                    showWiggleAnimation = index == 0 && showWiggleAnimation.value
                ).apply {
                    Modifier
                        .height(160.dp)
                        .offset(x = (eventCard.offset + 5).dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    onDragEnd(eventCard, swipedCards, weekEventCards, screenWidth)
                                },
                                onDrag = { _, dragAmount ->
                                    onDragChange(eventCard, dragAmount, showWiggleAnimation)
                                }
                            )
                        }
                }
            }
        }

        if (weekEventCards.size > 1) {
            ResetButton { resetCards(weekEventCards, swipedCards) }
        }
    }
}

@Composable
fun ResetButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Blue) // Replace with your primary color
        ) {
            Text(
                "Reset", // Replace with string resource
                color = Color.White // Replace with your onPrimary color
            )
        }
    }
}

fun resetCards(weekEventCards: MutableList<WeekEventCardModel>, swipedCards: MutableState<Int>) {
    weekEventCards.forEach { it.offset = 0f }
    swipedCards.value = 0
}

fun onDragChange(eventCard: WeekEventCardModel, dragAmount: Offset, showWiggleAnimation: MutableState<Boolean>) {
    if (dragAmount.x < 0) {
        eventCard.offset = dragAmount.x
    }
    if (showWiggleAnimation.value) {
        showWiggleAnimation.value = false
    }
}

fun onDragEnd(eventCard: WeekEventCardModel, swipedCards: MutableState<Int>, weekEventCards: List<WeekEventCardModel>, screenWidth: Dp) {
    if (abs(eventCard.offset) > screenWidth.value / 3) {
        eventCard.offset = -screenWidth.value
        swipedCards.value += 1
    } else {
        eventCard.offset = 0f
    }
    if (swipedCards.value == weekEventCards.size) {
        resetCards(weekEventCards.toMutableList(), swipedCards)
    }
}
