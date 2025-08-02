package tumble.app.tumble.presentation.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.enums.SchedulePreviewStatus
import tumble.app.tumble.domain.models.presentation.SearchPreviewModel
import tumble.app.tumble.presentation.components.buttons.BookmarkButton
import tumble.app.tumble.presentation.viewmodels.SearchPreviewViewModel
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.general.Info
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.screens.navigation.AppBarState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchPreviewSheet(
    viewModel: SearchPreviewViewModel = hiltViewModel(),
    searchPreviewModel: SearchPreviewModel,
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
){
    fun bookmark(){
        viewModel.bookmark(searchPreviewModel.scheduleId, searchPreviewModel.schoolId, searchPreviewModel.scheduleTitle)
    }

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = searchPreviewModel.scheduleId,
                actions = {
                    if (viewModel.status == SchedulePreviewStatus.LOADED)
                        BookmarkButton(bookmark = { bookmark() }, buttonState = viewModel.buttonState)
                },
                navigationAction = {
                    BackButton("") {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        when(viewModel.status){
            SchedulePreviewStatus.LOADED -> {
                SearchPreviewList(viewModel = viewModel)
            }
            SchedulePreviewStatus.LOADING -> {
                CustomProgressIndicator()
            }
            SchedulePreviewStatus.ERROR -> {
                Info(title = stringResource(id = R.string.error_something_wrong), image = null)
            }
            SchedulePreviewStatus.EMPTY -> {
                Info(title = stringResource(id = R.string.error_empty_schedule), image = null)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSchedule(searchPreviewModel.scheduleId, searchPreviewModel.schoolId)
    }
}