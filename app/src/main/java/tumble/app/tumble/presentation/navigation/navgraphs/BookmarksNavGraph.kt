package tumble.app.tumble.presentation.navigation.navgraphs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.views.bookmarks.Bookmarks
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navDeepLink

@Composable
fun BookmarksNavGraph(
    navController: NavHostController,
) {
    NavHost(navController, Routes.bookmarks) {
        bookmarks(navController)
        bookmarksDetails(navController)
    }
}

private fun NavGraphBuilder.bookmarks(navController: NavHostController) {
    composable(Routes.bookmarks) {
        Bookmarks()
    }
}

private fun NavGraphBuilder.bookmarksDetails(navController: NavHostController) {
    composable(
        Routes.bookmarksDetails,
        deepLinks = listOf(
            navDeepLink { uriPattern = Routes.BookmarksDetailsUri }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        // TODO: Show details for a specific bookmark item
        Text("Showing bookmarks?eventId=$id")
    }
}

