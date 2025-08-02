package tumble.app.tumble.presentation.screens.bookmarks.Week

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.utils.sortedEventOrder
import java.util.Date
import java.util.Locale

@Composable
fun WeekDays(
    day: Day?,
    weekDayDate: Date,
    onEventSelection: (Event) -> Unit
){
    val dateFormatterDay = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormatterDayMonth = SimpleDateFormat("d/MM", Locale.getDefault())

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${dateFormatterDay.format(weekDayDate)} ${
                    dateFormatterDayMonth.format(weekDayDate)}"
                    .replaceFirstChar { char -> char.uppercaseChar() },
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground)
                .weight(1f)
            )
        }
        if (day != null){
            day.events!!.sortedWith { a, b -> sortedEventOrder(a, b) }.forEach {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { onEventSelection(it) }
                ) {
                    WeekEvent(it)
                }
            }
        }else{
            EmptyEvent()
        }
    }
}

