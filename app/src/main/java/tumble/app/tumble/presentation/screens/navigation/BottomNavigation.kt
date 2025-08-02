package tumble.app.tumble.presentation.screens.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import tumble.app.tumble.presentation.navigation.Routes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import tumble.app.tumble.R

enum class BottomNavItem(val route: String, val icon: ImageVector, val title: String, val translationId : Int) {
    HOME(Routes.home, Icons.Rounded.Home, "Home", R.string.home),
    BOOKMARKS(Routes.bookmarks, Icons.Rounded.Bookmark, "Bookmarks", R.string.bookmark),
    SEARCH(Routes.search, Icons.Rounded.Search, "Search", R.string.search),
    ACCOUNT(Routes.account, Icons.Rounded.Person, "Account", R.string.account),
}

@Composable
fun BottomNavigation(
    currentNavGraph: MutableState<BottomNavItem>,
    navigateToRootOrSwitchGraph: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        BottomNavItem.values().forEach { item ->
            AddItem(
                navItem = item,
                isSelected = currentNavGraph.value == item,
                onSelected = navigateToRootOrSwitchGraph
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    navItem: BottomNavItem,
    isSelected: Boolean,
    onSelected: (BottomNavItem) -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        alwaysShowLabel = true,
        onClick = { onSelected(navItem) },
        icon = {
            Icon(imageVector = navItem.icon, contentDescription = navItem.title)
        }
    )
}