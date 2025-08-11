package com.tumble.kronoxtoapp.presentation.screens.home.available

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.models.WeekEventCardModel
import com.tumble.kronoxtoapp.presentation.screens.home.available.eventsCarousel.TodaysEventsCarousel

@Composable
fun TodaysEvents(
    eventsForToday: List<WeekEventCardModel>,
    swipedCards: Int
) {
    val localSwipedCards = remember { mutableIntStateOf(swipedCards) }
    val localEventsForToday = remember { eventsForToday.toMutableList() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.todays_events),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        if (localEventsForToday.isNotEmpty()) {
            TodaysEventsCarousel(
                swipedCards = localSwipedCards,
                weekEventCards = localEventsForToday
            )
        } else {
            Text(
                text = stringResource(R.string.no_events_for_today),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
