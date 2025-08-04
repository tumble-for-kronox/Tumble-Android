package com.tumble.kronoxtoapp.presentation.screens.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.runtime.MutableState

@Composable
fun BottomBar(
    homeNavController: NavHostController,
    bookmarksNavController: NavHostController,
    searchNavController: NavHostController,
    accountNavController: NavHostController,
    currentNavGraph: MutableState<BottomNavItem>,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
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
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground
                )
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
                restoreState = true
            }
        }
    }
}