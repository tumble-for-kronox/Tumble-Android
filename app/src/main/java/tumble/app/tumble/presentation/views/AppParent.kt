package tumble.app.tumble.presentation.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import tumble.app.tumble.presentation.navigation.navgraphs.AccountNavGraph
import tumble.app.tumble.presentation.navigation.navgraphs.BookmarksNavGraph
import tumble.app.tumble.presentation.navigation.navgraphs.HomeNavGraph
import tumble.app.tumble.presentation.navigation.navgraphs.SearchNavGraph
import tumble.app.tumble.presentation.viewmodels.ParentViewModel
import tumble.app.tumble.presentation.views.navigation.AppBarState
import tumble.app.tumble.presentation.views.navigation.BottomBar
import tumble.app.tumble.presentation.views.navigation.BottomNavItem
import tumble.app.tumble.presentation.views.navigation.TopBar
import tumble.app.tumble.ui.theme.TumbleTheme

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true, name = "AppParent preview")
@Composable
fun AppParent() {
    val viewModel: ParentViewModel = viewModel()

    val apperance = viewModel.appearance.collectAsState()

    val accountNavController = rememberNavController()
    val bookmarksNavController = rememberNavController()
    val homeNavController = rememberNavController()
    val searchNavController = rememberNavController()

    val currentNavGraph = remember { mutableStateOf(BottomNavItem.BOOKMARKS) }
    val currentNavController = when (currentNavGraph.value) {
        BottomNavItem.HOME -> homeNavController
        BottomNavItem.BOOKMARKS -> bookmarksNavController
        BottomNavItem.SEARCH -> searchNavController
        BottomNavItem.ACCOUNT -> accountNavController
    }

    TumbleTheme(userPreferences =  apperance.value) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            var appBarState by remember {
                mutableStateOf(AppBarState())
            }

            Scaffold(
                topBar = {
                    TopBar(appBarState)
                },
                bottomBar = {
                    BottomBar(
                        homeNavController,
                        bookmarksNavController,
                        searchNavController,
                        accountNavController,
                        currentNavGraph,
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize()
                ) {
                    when (currentNavGraph.value) {
                        BottomNavItem.HOME -> HomeNavGraph(homeNavController, { appBarState = it })
                        BottomNavItem.BOOKMARKS -> BookmarksNavGraph(bookmarksNavController, { appBarState = it })
                        BottomNavItem.SEARCH -> SearchNavGraph(searchNavController, { appBarState = it })
                        BottomNavItem.ACCOUNT -> AccountNavGraph(accountNavController, { appBarState = it })
                    }
                }
            }
        }
    }
}
