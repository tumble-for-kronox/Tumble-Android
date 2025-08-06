package com.tumble.kronoxtoapp.presentation.navigation.navgraphs

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.screens.settings.preferences.appearance.AppearanceSettings
import com.tumble.kronoxtoapp.presentation.screens.settings.bookmarks.BookmarksSettings
import com.tumble.kronoxtoapp.presentation.screens.settings.preferences.notifications.NotificationOffsetSettings
import com.tumble.kronoxtoapp.presentation.screens.settings.preferences.PreferencesScreen
import com.tumble.kronoxtoapp.presentation.screens.settings.SettingsScreen
import com.tumble.kronoxtoapp.presentation.screens.account.Account
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.events.EventBookings
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources.ResourceBookings
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources.ResourceSelection
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.utils.isoDateFormatter


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
            navDeepLink { uriPattern = Routes.AccountResourceDetailsUri },
        )
    ) { backStackEntry ->
        val arguments = backStackEntry.arguments
        val resourceId = arguments?.getString("resource_id")
        val dateString = arguments?.getString("iso_date_string")

        if (resourceId == null || dateString == null) {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
            return@composable
        }

        val selectedPickerDate = try {
            isoDateFormatter.parse(dateString)
        } catch (e: Exception) {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
            return@composable
        }

        if (selectedPickerDate == null) {
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
            return@composable
        }

        ResourceSelection(
            navController = navController,
            setTopNavState = setTopNavState,
            selectedPickerDate = selectedPickerDate,
            resourceId = resourceId
        )
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

