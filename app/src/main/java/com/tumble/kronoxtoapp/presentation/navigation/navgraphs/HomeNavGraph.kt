package com.tumble.kronoxtoapp.presentation.navigation.navgraphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import com.tumble.kronoxtoapp.presentation.screens.home.HomeScreen
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    NavHost(navController, Routes.home) {
        home(navController, onComposing)
        homeEventDetails(navController, onComposing)
        homeNews(navController)
        homeNewsDetails(navController)
    }
}

private fun NavGraphBuilder.home(navController: NavHostController, onComposing: (AppBarState) -> Unit) {
    composable(Routes.home) {
        HomeScreen(
            navController = navController,
            onComposing = onComposing
        )
    }
}

private fun NavGraphBuilder.homeNews(navController: NavHostController) {
    composable(Routes.homeNews) {
        Text("Showing home/news")
        // TODO: Show news view
    }
}

private fun NavGraphBuilder.homeEventDetails(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(
        Routes.homeEventDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.HomeEventDetailsUri },
        ),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
        }
    ) { backStackEntry ->
        val eventId = backStackEntry.arguments?.getString("event_id")

        if (eventId != null) {
            com.tumble.kronoxtoapp.presentation.screens.bookmarks.event.EventDetailsSheet(
                eventId = eventId,
                setTopNavState = setTopNavState,
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private fun NavGraphBuilder.homeNewsDetails(navController: NavHostController) {
    composable(
        Routes.homeNewsDetails, deepLinks = listOf(
        navDeepLink {
            uriPattern = Routes.HomeNewsDetailsUri
        }
    )) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        // TODO: Show details for a specific news item in sheet
        Text("Showing home/news?articleId=$id")
    }
}