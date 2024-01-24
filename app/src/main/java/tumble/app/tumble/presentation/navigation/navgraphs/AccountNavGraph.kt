package tumble.app.tumble.presentation.navigation.navgraphs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.account.Account

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
        Account()
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
        Text("Showing account/settings")
        // TODO: Show account settings
    }
}

private fun NavGraphBuilder.accountSettingsAppearance(navController: NavHostController) {
    composable(Routes.accountSettingsAppearance) {
        Text("Showing account/settings/appearance")
        // TODO: Show account settings appearance
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
        Text("Showing account/settings/notifications")
        // TODO: Show account settings notifications
    }
}

private fun NavGraphBuilder.accountSettingsBookmarks(navController: NavHostController) {
    composable(Routes.accountSettingsBookmarks) {
        Text("Showing account/settings/bookmarks")
        // TODO: Show account settings bookmarks
    }
}

private fun NavGraphBuilder.accountResources(navController: NavHostController) {
    composable(Routes.accountResources) {
        Text("Showing account/resources")
        // TODO: Show account resources
    }
}

private fun NavGraphBuilder.accountResourceDetails(navController: NavHostController) {
    composable(
        Routes.accountResourceDetails,
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        Text("Showing account/resources?resourceId=$id")
        // TODO: Show account resource details
    }
}

private fun NavGraphBuilder.accountEvents(navController: NavHostController) {
    composable(Routes.accountEvents) {
        Text("Showing account/events")
        // TODO: Show account events
    }
}

private fun NavGraphBuilder.accountEventDetails(navController: NavHostController) {
    composable(
        Routes.accountEventDetails,
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        Text("Showing account/events?eventId=$id")
        // TODO: Show account event details
    }
}

