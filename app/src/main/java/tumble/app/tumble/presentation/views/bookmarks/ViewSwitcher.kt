package tumble.app.tumble.presentation.views.bookmarks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.enums.ViewType
import tumble.app.tumble.domain.enums.viewTypeToStringResource
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewSwitcher(
    viewmodel: BookmarksViewModel = hiltViewModel(),
    pagerState: PagerState,
    viewType: State<ViewType>
){
    val routine = rememberCoroutineScope()
    Row (
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 5.dp)
            .width(280.dp)
            .background(color = MaterialTheme.colors.surface, RoundedCornerShape(30.dp))
    ){
        ViewType.values().forEach { type ->
            val isSelected = viewType.value == type
            Button(
                onClick = { routine.launch { pagerState.animateScrollToPage(type.ordinal) } },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSelected) MaterialTheme.colors.primary else Color.Transparent,
                    contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                ),
                elevation = null,
                contentPadding = PaddingValues(2.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = viewTypeToStringResource(viewType = type),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    }
}