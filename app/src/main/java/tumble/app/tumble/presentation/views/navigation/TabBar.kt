package tumble.app.tumble.presentation.views.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun TabBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val screens = listOf(
        TabBarScreen.Home,
        TabBarScreen.Search,
        TabBarScreen.Bookmarks,
        TabBarScreen.Account
    )

    BottomNavigation {
        screens.forEach {
            AddItem(
                screen = it,
                navController = navController,
                currentDestination = currentDestination
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: TabBarScreen,
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation icon")
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}