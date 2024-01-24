package tumble.app.tumble.presentation.navigation.navgraphs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.search.Search

@Composable
fun SearchNavGraph(
    navController: NavHostController,
) {
    NavHost(navController, Routes.search) {
        search(navController)
        searchDetails(navController)
    }
}

private fun NavGraphBuilder.search(navController: NavHostController) {
    composable(Routes.search) {
        Search()
    }
}

private fun NavGraphBuilder.searchDetails(navController: NavHostController) {
    composable(
        Routes.searchDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.SearchDetailsUri },
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        Text("Showing search?scheduleId=$id")
    }
}