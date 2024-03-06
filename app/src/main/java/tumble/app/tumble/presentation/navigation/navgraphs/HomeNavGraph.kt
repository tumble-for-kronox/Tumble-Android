package tumble.app.tumble.presentation.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation.NavGraphBuilder
import tumble.app.tumble.presentation.navigation.Routes
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink
import tumble.app.tumble.presentation.views.home.HomeScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
) {
    NavHost(navController, Routes.home) {
        home(navController)
        homeNews(navController)
        homeNewsDetails(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.home(navController: NavHostController) {
    composable(Routes.home) {
        HomeScreen()
    }
}

private fun NavGraphBuilder.homeNews(navController: NavHostController) {
    composable(Routes.homeNews) {
        Text("Showing home/news")
        // TODO: Show news view
    }
}

private fun NavGraphBuilder.homeNewsDetails(navController: NavHostController) {
    composable(Routes.homeNewsDetails, deepLinks = listOf(
        navDeepLink {
            uriPattern = Routes.HomeNewsDetailsUri
        }
    )) {backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        // TODO: Show details for a specific news item in sheet
        Text("Showing home/news?articleId=$id")
    }
}