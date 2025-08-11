package com.tumble.kronoxtoapp.presentation.screens.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.navigation.Routes

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val translationId: Int
) {
    HOME(Routes.home, Icons.Rounded.Home, "Home", R.string.home),
    BOOKMARKS(Routes.bookmarks, Icons.Rounded.Bookmark, "Bookmarks", R.string.bookmark),
    SEARCH(Routes.search, Icons.Rounded.Search, "Search", R.string.search),
    ACCOUNT(Routes.account, Icons.Rounded.AccountCircle, "Account", R.string.account),
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