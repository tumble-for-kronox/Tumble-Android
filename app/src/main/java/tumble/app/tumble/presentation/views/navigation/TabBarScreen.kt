package tumble.app.tumble.presentation.views.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : TabBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Bookmarks : TabBarScreen(
        route = "bookmarks",
        title = "Bookmarks",
        icon = Icons.Default.Bookmark
    )
    object Search : TabBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )
    object Account : TabBarScreen(
        route = "account",
        title = "Account",
        icon = Icons.Default.Person
    )
}
