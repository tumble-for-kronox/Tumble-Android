package tumble.app.tumble.presentation.navigation.navgraphs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.Settings.Preferences.AppearanceSettings.AppearanceSettings
import tumble.app.tumble.presentation.views.Settings.Bookmarks.BookmarksSettings
import tumble.app.tumble.presentation.views.Settings.Preferences.Notifications.NotificationOffsetSettings
import tumble.app.tumble.presentation.views.Settings.Preferences.PreferencesScreen
import tumble.app.tumble.presentation.views.Settings.SettingsScreen
import tumble.app.tumble.presentation.views.account.Account
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Events.EventBookings
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources.ResourceBookings
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources.ResourceSelection
import tumble.app.tumble.presentation.views.navigation.AppBarState


@Composable
fun AccountNavGraph(
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    NavHost(navController, Routes.account) {
        account(navController, setTopNavState)
        accountLogin(navController)
        accountSettings(navController, setTopNavState)
        accountSettingsAppearance(navController, setTopNavState)
        accountSettingsLanguage(navController)
        accountSettingsNotifications(navController, setTopNavState)
        accountSettingsBookmarks(navController, setTopNavState)
        accountResources(navController, setTopNavState)
        accountResourceDetails(navController, setTopNavState)
        accountEvents(navController, setTopNavState)
        accountEventDetails(navController, setTopNavState)
        accountSettingsPreferences(navController, setTopNavState)
    }
}

private fun NavGraphBuilder.account(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.account) {
        Account(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountLogin(navController: NavHostController) {
    composable(Routes.accountLogin) {
        Text("Showing account/login")
        // TODO: Show account login
    }
}

private fun NavGraphBuilder.accountSettings(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountSettings) {
        SettingsScreen(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountSettingsPreferences(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountSettingsPreferences) {
        PreferencesScreen(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountSettingsAppearance(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountSettingsAppearance) {
        AppearanceSettings( navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountSettingsLanguage(navController: NavHostController) {
    composable(Routes.accountSettingsLanguage) {
        Text("Showing account/settings/language")
        // TODO: Show account settings language
    }
}

private fun NavGraphBuilder.accountSettingsNotifications(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountSettingsNotifications) {
        NotificationOffsetSettings(navController =  navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountSettingsBookmarks(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountSettingsBookmarks) {
        BookmarksSettings(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountResources(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountResources) {
        ResourceBookings(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountResourceDetails(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(
        Routes.accountResourceDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.AccountResourceDetailsUri},
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        ResourceSelection(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountEvents(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.accountEvents) {
        EventBookings(navController =  navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.accountEventDetails(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(
        Routes.accountEventDetails,
    ) { backStackEntry ->
//        val id = backStackEntry.arguments?.getString("id")
//        EventBookings(navController =  navController)
    }
}

