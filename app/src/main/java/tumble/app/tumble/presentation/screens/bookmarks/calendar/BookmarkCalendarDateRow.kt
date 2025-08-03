package tumble.app.tumble.presentation.screens.bookmarks.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dateRow(
    inMonth: (Int) -> Float,
    localDate: LocalDate,
    getColor:@Composable (LocalDate) -> Color,
    onClick: (LocalDate) -> Unit
): LocalDate {

    var localLocalDate = localDate
    Row (horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        repeat(7) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalendarDate(
                    inMonth = inMonth,
                    localDate = localLocalDate,
                    getColor = getColor,
                    onClick = onClick
                )
                EventIndicator(localDate = localLocalDate)
            }
            localLocalDate = localLocalDate.plusDays(1)
        }
    }
    return localLocalDate
}