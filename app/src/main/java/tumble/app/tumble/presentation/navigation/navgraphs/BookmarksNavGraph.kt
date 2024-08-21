package tumble.app.tumble.presentation.navigation.navgraphs

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.bookmarks.Bookmarks
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.views.bookmarks.EventDetails.EventDetailsSheet


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookmarksNavGraph(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = Routes.bookmarks) {
        bookmarksDetails(navController)
        bookmarks(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.bookmarks(navController: NavHostController) {
    composable(Routes.bookmarks) {
        Bookmarks(navController = navController)
    }
}

private fun NavGraphBuilder.bookmarksDetails(navController: NavHostController) {
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
        EventDetailsSheet(event = AppController.shared.eventSheet!!.event, navController)
    }
}