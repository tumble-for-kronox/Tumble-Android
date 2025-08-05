package com.tumble.kronoxtoapp.presentation.navigation.navgraphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.Bookmarks
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import com.tumble.kronoxtoapp.observables.AppController
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.event.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState


@Composable
fun BookmarksNavGraph(
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    NavHost(navController, startDestination = Routes.bookmarks) {
        bookmarksDetails(navController, setTopNavState)
        bookmarks(navController, setTopNavState)
    }
}

private fun NavGraphBuilder.bookmarks(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.bookmarks) {
        Bookmarks(
            navController = navController,
            setTopNavState = setTopNavState
        )
    }
}

private fun NavGraphBuilder.bookmarksDetails(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(
        Routes.bookmarksDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.BookmarksDetailsUri }
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
            EventDetailsSheet(
                eventId = eventId,
                setTopNavState = setTopNavState,
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}