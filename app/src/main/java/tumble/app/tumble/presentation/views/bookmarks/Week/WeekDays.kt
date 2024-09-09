package tumble.app.tumble.presentation.views.bookmarks.Week

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.domain.models.realm.Event
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

    Column {
        Row (
            modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${dateFormatterDay.format(weekDayDate)} ${
                    dateFormatterDayMonth.format(weekDayDate)}"
                    .replaceFirstChar { char -> char.uppercaseChar() },
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.height(1.dp)
                .background(MaterialTheme.colors.onBackground)
                .weight(1f)
            )
        }
        if (day != null){
            day.events!!.sortedWith{a,b -> sortedEventOrder(a,b)}.forEach{
                Surface(
                    color = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20))
                        .padding(vertical = 5.dp)
                        .clickable{ onEventSelection(it) }
                ){
                    WeekEvent(it)
                }
            }
        }else{
            Surface(
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20))
            ) {
                EmptyEvent()
            }
        }
    }
}

