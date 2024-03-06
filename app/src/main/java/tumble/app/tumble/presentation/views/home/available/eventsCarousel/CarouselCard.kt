package tumble.app.tumble.presentation.views.home.available.eventsCarousel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.components.buttons.VerboseEventButtonLabel
import tumble.app.tumble.extensions.presentation.wiggle
import tumble.app.tumble.presentation.models.WeekEventCardModel

@Composable
fun CarouselCard(
    event: Event,
    index: Int,
    eventsForToday: List<WeekEventCardModel>,
    swipedCards: Int,
    showWiggleAnimation: Boolean
) {
    val rotation = getCardRotation(index = index, eventsForToday = eventsForToday)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .width(getCardWidth())
                .height(getCardHeight(index, swipedCards))
                .graphicsLayer {
                    rotationZ = rotation
                    translationX = getCardOffset(index, swipedCards).toPx()
                }
                .let {
                    if (showWiggleAnimation) it.wiggle() else it
                }
                .let {
                    if (index != eventsForToday.size - 1)
                        it.shadow(if ((index - swipedCards) <= 2) 2.dp else 0.dp, RoundedCornerShape(15.dp), true, Color.Black.copy(alpha = 0.1f))
                    else it
                }
        ) {
            VerboseEventButtonLabel(event = event) // You need to define this composable
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun getCardRotation(index: Int, eventsForToday: List<WeekEventCardModel>): Float {
    val boxWidth = LocalConfiguration.current.screenWidthDp / 3
    val offset = eventsForToday[index].offset
    val angle = 8f
    return (offset / boxWidth) * angle
}

fun getCardHeight(index: Int, swipedCards: Int): Dp {
    val height = 160.dp
    val cardHeight = if ((index - swipedCards) <= 3) (index - swipedCards) * 35 else 30
    return height - cardHeight.dp
}

@Composable
fun getCardWidth(): Dp {
    val boxWidth = LocalConfiguration.current.screenWidthDp - 60
    return boxWidth.dp
}

fun getCardOffset(index: Int, swipedCards: Int): Dp {
    return if ((index - swipedCards) <= 2) (index - swipedCards) * 13.dp else 0.dp
}
