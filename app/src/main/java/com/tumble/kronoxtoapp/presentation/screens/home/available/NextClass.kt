package com.tumble.kronoxtoapp.presentation.screens.home.available

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.presentation.formatDate
import com.tumble.kronoxtoapp.other.extensions.presentation.toColor
import com.tumble.kronoxtoapp.presentation.components.buttons.CompactEventButtonLabel

@Composable
fun NextClass(nextClass: Event?, onEventSelection: (Event) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.next_class),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            nextClass?.let {
                Text(
                    text = it.from.formatDate() ?: "",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (nextClass?.course != null) {
            val color = nextClass.course?.color?.toColor() ?: Color.White
            CompactEventButtonLabel(
                event = nextClass,
                color = color,
                onEventSelection = onEventSelection
            )
        } else {
            Text(
                text = stringResource(R.string.no_upcoming_class),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        }
    }
}
