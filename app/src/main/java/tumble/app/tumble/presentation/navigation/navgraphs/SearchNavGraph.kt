package tumble.app.tumble.presentation.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.navigation.AppBarState
import tumble.app.tumble.presentation.views.search.Search
import tumble.app.tumble.presentation.views.search.SearchPreviewSheet

@Composable
fun SearchNavGraph(
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    NavHost(navController, Routes.search) {
        search(navController, setTopNavState)
        searchDetails(navController, setTopNavState)
    }
}

private fun NavGraphBuilder.search(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(Routes.search) {
        Search(navController = navController, setTopNavState = setTopNavState)
    }
}

private fun NavGraphBuilder.searchDetails(navController: NavHostController, setTopNavState: (AppBarState) -> Unit) {
    composable(
        Routes.searchDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.SearchDetailsUri },
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

        SearchPreviewSheet(searchPreviewModel = AppController.shared.searchPreview!!, navController = navController, setTopNavState = setTopNavState)

        val id = backStackEntry.arguments?.getString("id")
    }
}