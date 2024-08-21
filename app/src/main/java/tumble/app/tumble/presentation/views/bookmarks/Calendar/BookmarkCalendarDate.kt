package tumble.app.tumble.presentation.views.bookmarks.Calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDate (
    inMonth: (Int) -> Float,
    localDate: LocalDate,
    getColor:@Composable (LocalDate) -> Color,
    onClick: (LocalDate) -> Unit
) {
     Box(contentAlignment = Alignment.Center,) {
        But(
            localDate = localDate,
            onClick = onClick,
            getColor =  getColor
        )
        Text(
            text = localDate.dayOfMonth.toString(),
            color = MaterialTheme.colors.onBackground.copy(
                alpha = inMonth(localDate.month.value)
            ),
            fontSize = 20.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun But(
    localDate: LocalDate,
    onClick: (LocalDate) -> Unit,
    getColor: @Composable (LocalDate) -> Color,
){

    Surface(modifier = Modifier
        .size(40.dp)
        .noRippleClickable { onClick(localDate) },
        shape = CircleShape,
        color = getColor(localDate)){}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventIndicator(
    viewModel: BookmarksViewModel = hiltViewModel(),
    localDate: LocalDate
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 1.dp)
            .height(6.dp)
    ) {

        val events = viewModel.bookmarkData.calendarEventsByDate.getOrDefault(localDate, emptyList())
        if (events.isNotEmpty()) {
            events.forEach {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(6.dp)
                        .background(color = MaterialTheme.colors.primary, CircleShape)
                )
            }
        }
    }
}