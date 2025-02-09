package tumble.app.tumble.presentation.views.home.available

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.models.WeekEventCardModel

@Composable
fun HomeAvailable(
    eventsForToday: MutableState<List<WeekEventCardModel>>,
    nextClass: Event?,
    swipedCards: MutableState<Int>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TodaysEvents(
            eventsForToday = eventsForToday.value,
            swipedCards = swipedCards.value
        )
        NextClass(nextClass = nextClass)
        Spacer(modifier = Modifier.weight(1f))
    }
}