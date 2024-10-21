package tumble.app.tumble.presentation.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.bookmarks.Bookmarks
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.views.bookmarks.EventDetails.EventDetailsSheet
import tumble.app.tumble.presentation.views.navigation.AppBarState


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
        Bookmarks(navController = navController, setTopNavState = setTopNavState)
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
        val id = backStackEntry.arguments?.getString("id")
        EventDetailsSheet(event = AppController.shared.eventSheet!!.event)
    }
}