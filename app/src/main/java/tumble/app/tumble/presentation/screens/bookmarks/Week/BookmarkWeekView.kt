package tumble.app.tumble.presentation.screens.bookmarks.Week

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.viewmodels.BookmarksViewModel
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun BookmarkWeekView(
    viewModel: BookmarksViewModel = hiltViewModel(),
    onEventSelection: (Event) -> Unit,
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { viewModel.bookmarkData.weekStartDates.size }
    )

    val indicatorScrollState = rememberLazyListState()

    var userScrollEnabled by  remember {
        mutableStateOf(true)
    }

    val updateUserScroll = {new:Boolean ->
        userScrollEnabled = new
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 3,
                    pageSpacing = 0.dp,
                    verticalAlignment = Alignment.Top,
                    userScrollEnabled = userScrollEnabled,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            handleLeftSwipeOnFirstPage(pointerInputScope = this, pagerState = pagerState, updateUserScroll = updateUserScroll)
                        }
                ) { page ->
                    var showContend by remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(key1 = pagerState.isScrollInProgress) {
                        snapshotFlow {
                            Pair(
                                pagerState.isScrollInProgress,
                                abs(pagerState.settledPage - page)
                            )
                        }.collect { (scrollInProgress, diff) ->
                            if (!scrollInProgress && (diff in 0..1)) {
                                showContend = true
                                cancel()
                            }
                        }
                    }
                    if (showContend) {
                        WeekPage(
                            page = page,
                            onEventSelection = onEventSelection
                        )
                    } else {
                        CustomProgressIndicator()
                    }
                }
            }
            Box(modifier = Modifier.padding(bottom = 64.dp)) {
                PageIndicator(
                    indicatorScrollState = indicatorScrollState,
                    pagerState = pagerState,
                )
            }
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        val currentPageIndication = pagerState.currentPage
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
        val firstVisibleIndex = indicatorScrollState.firstVisibleItemIndex

        if (currentPageIndication > lastVisibleIndex - 2) {
            indicatorScrollState.animateScrollToItem(currentPageIndication - size + 3)
        } else if (currentPageIndication <= firstVisibleIndex + 2) {
            indicatorScrollState.animateScrollToItem(Math.max(currentPageIndication - 2, 0))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun handleLeftSwipeOnFirstPage(pointerInputScope: PointerInputScope, pagerState: PagerState, updateUserScroll: (Boolean) -> Unit){
    pointerInputScope.let {
        it.awaitEachGesture {
            awaitFirstDown(pass = PointerEventPass.Initial)
            do {
                val event: PointerEvent = awaitPointerEvent(
                    pass = PointerEventPass.Initial
                )
                event.changes.forEach {
                    val diffX = it.position.x - it.previousPosition.x

                    if (diffX > 0 && pagerState.currentPage == 0) {
                        updateUserScroll(false)
                    } else {
                        updateUserScroll(true)
                    }
                }
            } while (event.changes.any { it.pressed })
        }
    }
}