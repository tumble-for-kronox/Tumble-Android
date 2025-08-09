package com.tumble.kronoxtoapp.presentation.navigation.navgraphs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.Bookmarks
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import com.tumble.kronoxtoapp.presentation.screens.bookmarks.event.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState


@SuppressLint("RestrictedApi")
@Composable
fun BookmarksNavGraph(
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    Log.d("BookmarksNavGraph", "Routes.bookmarks = '${Routes.bookmarks}'")
    Log.d("BookmarksNavGraph", "NavController current destination: ${navController.currentDestination?.route}")

    NavHost(
        navController = navController,
        startDestination = Routes.bookmarks
    ) {
        Log.d("BookmarksNavGraph", "Inside NavHost builder")
        bookmarks(navController, setTopNavState)
        bookmarksDetails(navController, setTopNavState)
    }

    LaunchedEffect(navController) {
        Log.d("BookmarksNavGraph", "NavController graph: ${navController.graph}")
        Log.d("BookmarksNavGraph", "NavController graph nodes: ${navController.graph.nodes}")
    }
}

private fun NavGraphBuilder.bookmarks(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    Log.d("BookmarksNavGraph", "Calling :Bookmarks")
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