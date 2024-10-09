package tumble.app.tumble.presentation.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.views.Settings.AppearanceSettings.AppearanceSettings
import tumble.app.tumble.presentation.views.Settings.Bookmarks.BookmarksSettings
import tumble.app.tumble.presentation.views.Settings.Notifications.NotificationOffsetSettings
import tumble.app.tumble.presentation.views.Settings.SettingsScreen
import tumble.app.tumble.presentation.views.account.Account
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events.EventBookings
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources.ResourceBookings

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountNavGraph(
    navController: NavHostController,
) {
    NavHost(navController, Routes.account) {
        account(navController)
        accountLogin(navController)
        accountSettings(navController)
        accountSettingsAppearance(navController)
        accountSettingsLanguage(navController)
        accountSettingsNotifications(navController)
        accountSettingsBookmarks(navController)
        accountResources(navController)
        accountResourceDetails(navController)
        accountEvents(navController)
        accountEventDetails(navController)
    }
}

private fun NavGraphBuilder.account(navController: NavHostController) {
    composable(Routes.account) {
        val viewModel: AccountViewModel = hiltViewModel()
        Account(viewModel = viewModel, navController = navController)
    }
}

private fun NavGraphBuilder.accountLogin(navController: NavHostController) {
    composable(Routes.accountLogin) {
        Text("Showing account/login")
        // TODO: Show account login
    }
}

private fun NavGraphBuilder.accountSettings(navController: NavHostController) {
    composable(Routes.accountSettings) {
        SettingsScreen(navController = navController)
    }
}

private fun NavGraphBuilder.accountSettingsAppearance(navController: NavHostController) {
    composable(Routes.accountSettingsAppearance) {
        AppearanceSettings( navController = navController)
    }
}

private fun NavGraphBuilder.accountSettingsLanguage(navController: NavHostController) {
    composable(Routes.accountSettingsLanguage) {
        Text("Showing account/settings/language")
        // TODO: Show account settings language
    }
}

private fun NavGraphBuilder.accountSettingsNotifications(navController: NavHostController) {
    composable(Routes.accountSettingsNotifications) {
        NotificationOffsetSettings(navController =  navController)
    }
}

private fun NavGraphBuilder.accountSettingsBookmarks(navController: NavHostController) {
    composable(Routes.accountSettingsBookmarks) {
        BookmarksSettings(navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.accountResources(navController: NavHostController) {
    composable(Routes.accountResources) {
        //ResourceBookings(navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.accountResourceDetails(navController: NavHostController) {
    composable(
        Routes.accountResourceDetails,
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        ResourceBookings(navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.accountEvents(navController: NavHostController) {
    composable(Routes.accountEvents) {
        //EventBookings(navController =  navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.accountEventDetails(navController: NavHostController) {
    composable(
        Routes.accountEventDetails,
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        EventBookings(navController =  navController)
    }
}

