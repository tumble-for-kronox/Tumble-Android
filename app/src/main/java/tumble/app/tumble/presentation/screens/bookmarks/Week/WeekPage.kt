package tumble.app.tumble.presentation.screens.bookmarks.Week

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import java.util.Calendar
import tumble.app.tumble.R

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
        .fillMaxWidth()
        .padding(horizontal = 15.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row (modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.weight(1f))
            Text(String.format("w. %s", weekOfYear), fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        }
        if (weekDays.isEmpty()){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 42.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.no_events_for_week),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    painter = painterResource(id = R.drawable.girl_relaxing),
                    contentDescription = "woman_lounging",
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .aspectRatio(1f)
                )
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
        Spacer(modifier = Modifier.height(100.dp))
    }
}