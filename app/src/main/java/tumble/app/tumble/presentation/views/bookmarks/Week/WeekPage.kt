package tumble.app.tumble.presentation.views.bookmarks.Week

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import java.util.Calendar
import tumble.app.tumble.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekPage(
    viewModel: BookmarksViewModel = hiltViewModel(),
    page: Int,
    onEventSelection: (Event) -> Unit
){

    val weekStart by remember {
        mutableStateOf(viewModel.bookmarkData.weekStartDates[page])
    }
    val weekDays = viewModel.bookmarkData.weeks
    .getOrDefault(
        Calendar
            .getInstance()
            .apply { time = weekStart }
            .get(Calendar.WEEK_OF_YEAR),
        listOf()
    )
    val weekOfYear = Calendar.getInstance().apply { time = weekStart }.get(Calendar.WEEK_OF_YEAR)

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()) {
            Row (modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.weight(1f))
                Text(String.format("w. %s", weekOfYear), fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.onBackground)
            }
            if (weekDays.isEmpty()){
                Column {
                    Text(text = stringResource(id = R.string.no_events_for_week), color = MaterialTheme.colors.onBackground, style = MaterialTheme.typography.body2)
                }
            }else{
                for (dayOfWeek in 0..6){
                    val weekDayDate = Calendar.getInstance().apply {
                        time = weekStart
                        add(Calendar.DAY_OF_YEAR, dayOfWeek)
                    }.time
                    WeekDays(weekDays.getOrNull(dayOfWeek), weekDayDate, onEventSelection)
                }
            }
            Spacer(modifier = Modifier.height(60.dp))

    }
}