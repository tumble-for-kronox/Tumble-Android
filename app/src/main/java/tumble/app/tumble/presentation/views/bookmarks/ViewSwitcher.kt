package tumble.app.tumble.presentation.views.bookmarks

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.enums.ViewType
import tumble.app.tumble.domain.enums.viewTypeToStringResource
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewSwitcher(
    viewmodel: BookmarksViewModel = hiltViewModel(),
    pagerState: PagerState,
    viewType: State<ViewType>
) {
    val routine = rememberCoroutineScope()
    val selectedIndex = viewType.value.ordinal
    val switcherSize = 280
    val padding = 3
    val pillSize = (switcherSize - padding * (ViewType.values().size + 1)) / ViewType.values().size

    val pillOffset = animateDpAsState(
        targetValue = ((selectedIndex - 1) * pillSize + (selectedIndex - 1) * padding).dp,
    )

    Box(
        modifier = Modifier
            .width(switcherSize.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(30.dp)
            )
            .background(color = MaterialTheme.colors.surface, RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset(x = pillOffset.value)
                .width(pillSize.dp)
                .height(30.dp)
                .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(30.dp))
        )
        Row {
            ViewType.values().forEach { type ->
                val isSelected = viewType.value == type
                val buttonColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                    animationSpec = spring()
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .height(30.dp)
                        .noRippleClickable {
                            routine.launch {
                                pagerState.animateScrollToPage(type.ordinal)
                            }
                        }
                ) {
                    Text(
                        text = viewTypeToStringResource(viewType = type),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center),
                        color = buttonColor
                    )
                }
            }
        }

    }
}
