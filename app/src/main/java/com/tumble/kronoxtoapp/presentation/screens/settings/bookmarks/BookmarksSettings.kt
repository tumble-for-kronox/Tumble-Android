package com.tumble.kronoxtoapp.presentation.screens.settings.bookmarks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.viewmodels.SettingsViewModel
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsList
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@Composable
fun BookmarksSettings(
    parentViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.bookmark)
    val backTitle = stringResource(R.string.accountSettings)

    val schedules = parentViewModel.bookmarks.collectAsState()

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = pageTitle,
                navigationAction = {
                    BackButton(backTitle) {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    if (schedules.value.isNotEmpty()) {
        SettingsList {
            schedules.value.forEach{ schedule ->
                BookmarkSettingsRow(
                    schedule = schedule,
                    onDelete = {
                        parentViewModel.deleteBookmark(schedule)
                    },
                    onToggle = { visibility ->
                        parentViewModel.updateBookmarkVisibility(visibility, schedule)
                    }
                )
            }
        }
    } else {
        Info(title = stringResource(R.string.no_bookmarks), image = null)
    }
}
