package tumble.app.tumble.presentation.views.bookmarks.Week

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    indicatorScrollState: LazyListState,
    pagerState: PagerState,
){
    val routine = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .width(260.dp)
            .background(
                color = MaterialTheme.colors.surface,
                RoundedCornerShape(30.dp)
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyRow(
            state = indicatorScrollState,
            modifier = Modifier
                .width(((6 + 16) * 3 + 5 * (10 + 16)).dp)
                .background(color = MaterialTheme.colors.surface, RoundedCornerShape(30.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                item(key = "item$iteration") {
                    val alpha = if (pagerState.currentPage == iteration) 1f else 0.2f
                    val size by indicatorSize(
                        currentPage = pagerState.currentPage,
                        indicatorScrollState = indicatorScrollState,
                        iteration = iteration
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = alpha),
                                CircleShape
                            )
                            .size(size)
                            .clickable { routine.launch { pagerState.animateScrollToPage(iteration) } }
                    )
                }
            }
        }
    }
}

@Composable
fun indicatorSize(currentPage: Int, indicatorScrollState: LazyListState, iteration: Int ): State<Dp> {
    val firstVisibleIndex by remember { derivedStateOf { indicatorScrollState.firstVisibleItemIndex } }
    val lastVisibleIndex =
        indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            ?: 0
    val firstVisibleIndexIs0 = if(firstVisibleIndex == 0) 0 else 2

    return animateDpAsState(
        targetValue = if (iteration == currentPage || iteration in firstVisibleIndex + firstVisibleIndexIs0 ..lastVisibleIndex - 2) {
            10.dp
        } else if (iteration == lastVisibleIndex || iteration == firstVisibleIndex){
            6.dp
        } else{
            8.dp
        }
    )
}