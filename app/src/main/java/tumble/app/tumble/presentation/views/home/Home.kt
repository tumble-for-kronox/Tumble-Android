package tumble.app.tumble.presentation.views.home
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.HomeStatus
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.presentation.viewmodels.HomeViewModelNew
import tumble.app.tumble.presentation.viewmodels.ParentViewModel
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.presentation.views.home.available.HomeAvailable
import tumble.app.tumble.presentation.views.home.news.News
import tumble.app.tumble.presentation.views.home.news.NewsSheet

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModelNew = hiltViewModel(),
    parentViewModel: ParentViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
//    val schedules by viewModel.schedules(query = "YOUR_QUERY_HERE").collectAsState(initial = listOf())
    val newsStatus = viewModel.newsSectionStatus
    val homeStatus = viewModel.status

    val news = viewModel.news
    val showSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home)) },
                backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 12.dp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            if (newsStatus == PageState.LOADED) {
                News(news = news?.take(4), showOverlay = showSheet)
            }
            Spacer(Modifier.weight(1f))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                when (homeStatus) {
                    HomeStatus.AVAILABLE -> HomeAvailable(
                        eventsForToday = viewModel.todaysEventsCards,
                        nextClass = viewModel.nextClass,
                        swipedCards = viewModel.swipedCards
                    )
                    HomeStatus.LOADING -> CustomProgressIndicator()
                    HomeStatus.NO_BOOKMARKS -> HomeNoBookmarks()
                    HomeStatus.NOT_AVAILABLE -> HomeNotAvailable()
                    HomeStatus.ERROR -> Info(
                        title = stringResource(id = R.string.error_something_wrong),
                        image = null
                    )
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
    if (showSheet.value) {
        ModalBottomSheet(onDismissRequest = { showSheet.value = false }) {
            NewsSheet(news = news, sheetState = sheetState, showSheet = showSheet)
        }
    }
}
