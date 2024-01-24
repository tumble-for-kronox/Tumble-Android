package tumble.app.tumble.presentation.views.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.runtime.MutableState
import androidx.compose.material.MaterialTheme

@Composable
fun BottomBar(
    homeNavController: NavHostController,
    bookmarksNavController: NavHostController,
    searchNavController: NavHostController,
    accountNavController: NavHostController,
    currentNavGraph: MutableState<BottomNavItem>,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.White
    ) {
        BottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = null) },
                selected = currentNavGraph.value == item,
                onClick = {
                    if (currentNavGraph.value != item) {
                        currentNavGraph.value = item
                        when (item) {
                            BottomNavItem.HOME -> navigateTo(homeNavController, item)
                            BottomNavItem.BOOKMARKS -> navigateTo(bookmarksNavController, item)
                            BottomNavItem.SEARCH -> navigateTo(searchNavController, item)
                            BottomNavItem.ACCOUNT -> navigateTo(accountNavController, item)
                        }
                    }
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

private fun navigateTo(navController: NavHostController, item: BottomNavItem) {
    item.route.let { route ->
        val currentGraph = navController.currentBackStackEntry?.destination?.parent
        if (currentGraph != null) {
            navController.navigate(route) {
                popUpTo(currentGraph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
}
