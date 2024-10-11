package tumble.app.tumble.presentation.views.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tumble.app.tumble.extensions.models.flatten
import tumble.app.tumble.extensions.models.ordered
import tumble.app.tumble.extensions.models.toRealmEvent
import tumble.app.tumble.presentation.components.buttons.VerboseEventButtonLabel
import tumble.app.tumble.presentation.viewmodels.SearchPreviewViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchPreviewList(
    viewModel: SearchPreviewViewModel
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 2.5.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
        viewModel.schedule?.flatten()?.ordered()?.forEach{day ->
            if (day.events.isNotEmpty()) {
                item {
                    Column {
                        DayResponseHeader(day = day)
                        day.events.forEach { event ->
                            VerboseEventButtonLabel(event = event.toRealmEvent(viewModel.courseColorsForPreview))
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}