package com.tumble.kronoxtoapp.presentation.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Tab(
    val icon: ImageVector,
    val title: String,
) {
    Home(Icons.Rounded.Home, "Home"),
    Bookmarks(Icons.Rounded.Bookmark, "Bookmarks"),
    Search(Icons.Rounded.Search, "Search"),
    Account(Icons.Rounded.AccountCircle, "Account"),
}
