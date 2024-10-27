package tumble.app.tumble.presentation.views.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.navigation.getCurrentRoute
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.views.account.SettingsButton
import tumble.app.tumble.presentation.views.account.SignOutButton

@Composable
fun TopBarBackButton(currentNavController: NavHostController) {
    val route = getCurrentRoute(currentNavController);

    return when(route) {
        Routes.accountResources,
        Routes.accountEvents,
        Routes.accountSettings -> BackButton(
            { currentNavController.popBackStack() },
            stringResource(R.string.account)
        )
        Routes.accountSettingsBookmarks,
        Routes.accountSettingsNotifications,
        Routes.accountSettingsAppearance,
        Routes.accountSettingsLanguage -> BackButton(
            { currentNavController.popBackStack() },
            stringResource(R.string.accountSettings)
        )
        Routes.accountResourceDetails -> BackButton(
            { currentNavController.popBackStack()},
            stringResource(R.string.resources)
        )

        else -> {}
    }
}

@Composable
fun TopBarTitle(currentNavController: NavHostController) {
    val route = getCurrentRoute(currentNavController)

    return when (route) {
        Routes.home -> Text(stringResource(R.string.home))
        Routes.bookmarks -> Text(stringResource(R.string.bookmark))
        Routes.search -> Text(stringResource(R.string.search))
        Routes.account -> Text(stringResource(R.string.account))
        Routes.accountSettings -> Text(stringResource(R.string.accountSettings))
        Routes.accountSettingsBookmarks -> Text(stringResource(R.string.bookmark))
        Routes.accountSettingsAppearance -> Text(stringResource(R.string.appearance))
        Routes.accountSettingsLanguage -> Text(stringResource(R.string.app_language))
        Routes.accountSettingsNotifications -> Text(stringResource(R.string.notification_offset))
        Routes.searchDetails -> Text(stringResource(R.string.search))
        Routes.accountEvents -> Text(stringResource(R.string.events))
        Routes.accountResources -> Text(stringResource(R.string.resources))
        Routes.accountResourceDetails -> Text(stringResource(R.string.rooms))
        else -> Text(stringResource(R.string.app_name))
    }
}

@Composable
fun TopBarActions(currentNavController: NavHostController) {
    val route = getCurrentRoute(currentNavController)
    val accountViewModel: AccountViewModel? = if (route == Routes.account) {
        hiltViewModel()
    } else null

    val authStatus = accountViewModel?.authStatus?.collectAsState()

   return when (route) {
        Routes.account -> {
            if (authStatus?.value == AccountViewModel.AuthStatus.AUTHORIZED) {
                SignOutButton { accountViewModel.openLogOutConfirm() }
            }
            SettingsButton {
                currentNavController.navigate(Routes.accountSettings)
            }
        }

       else -> {}
   }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentNavHost: NavHostController) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(currentNavHost) },
        actions = { Row { TopBarActions(currentNavHost) } },
        navigationIcon = { TopBarBackButton(currentNavHost) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colors.background,
            titleContentColor = MaterialTheme.colors.onBackground,
            actionIconContentColor = MaterialTheme.colors.primary,
            navigationIconContentColor = MaterialTheme.colors.primary
        )
    )
}