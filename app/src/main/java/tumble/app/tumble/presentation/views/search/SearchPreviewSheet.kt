package tumble.app.tumble.presentation.views.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.enums.SchedulePreviewStatus
import tumble.app.tumble.domain.models.presentation.SearchPreviewModel
import tumble.app.tumble.presentation.components.buttons.BookmarkButton
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.viewmodels.SearchPreviewViewModel
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.R
import tumble.app.tumble.presentation.views.navigation.AppBarState


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchPreviewSheet(
    viewModel: SearchPreviewViewModel = hiltViewModel(),
    searchPreviewModel: SearchPreviewModel,
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
){
    fun bookmark(){
        viewModel.bookmark(searchPreviewModel.scheduleId, searchPreviewModel.schoolId)
    }

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = searchPreviewModel.scheduleId
            )
        )
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
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

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        if (viewModel.status == SchedulePreviewStatus.LOADED)
            BookmarkButton(bookmark = { bookmark() }, buttonState = viewModel.buttonState)
        else
            Box {}

        CloseCoverButton(
            onClick = { navController.popBackStack() },
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getSchedule(searchPreviewModel.scheduleId, searchPreviewModel.schoolId)
    }
}