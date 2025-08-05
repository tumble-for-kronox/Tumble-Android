package com.tumble.kronoxtoapp.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.tumble.kronoxtoapp.presentation.navigation.navgraphs.AccountNavGraph
import com.tumble.kronoxtoapp.presentation.navigation.navgraphs.BookmarksNavGraph
import com.tumble.kronoxtoapp.presentation.navigation.navgraphs.HomeNavGraph
import com.tumble.kronoxtoapp.presentation.navigation.navgraphs.SearchNavGraph
import com.tumble.kronoxtoapp.presentation.viewmodels.ParentViewModel
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.screens.navigation.BottomBar
import com.tumble.kronoxtoapp.presentation.screens.navigation.BottomNavItem
import com.tumble.kronoxtoapp.presentation.screens.navigation.TopBar
import com.tumble.kronoxtoapp.theme.TumbleTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppParent() {
    val viewModel: ParentViewModel = viewModel()
    val appearance = viewModel.appearance.collectAsState()

    val accountNavController = rememberNavController()
    val bookmarksNavController = rememberNavController()
    val homeNavController = rememberNavController()
    val searchNavController = rememberNavController()

    val currentTab = remember { mutableStateOf(BottomNavItem.HOME) }

    TumbleTheme(userPreferences =  appearance.value) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var appBarState by remember {
                mutableStateOf(AppBarState())
            }

            Scaffold(
                topBar = {
                    TopBar(appBarState)
                },
                bottomBar = {
                    Column {
                        HorizontalDivider()
                        BottomBar(
                            homeNavController,
                            bookmarksNavController,
                            searchNavController,
                            accountNavController,
                            currentTab,
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize()
                ) {
                    when (currentTab.value) {
                        BottomNavItem.HOME -> HomeNavGraph(homeNavController) { appBarState = it }
                        BottomNavItem.BOOKMARKS -> BookmarksNavGraph(bookmarksNavController) {
                            appBarState = it
                        }

                        BottomNavItem.SEARCH -> SearchNavGraph(searchNavController) {
                            appBarState = it
                        }

                        BottomNavItem.ACCOUNT -> AccountNavGraph(accountNavController) {
                            appBarState = it
                        }
                    }
                }
            }
        }
    }
}
