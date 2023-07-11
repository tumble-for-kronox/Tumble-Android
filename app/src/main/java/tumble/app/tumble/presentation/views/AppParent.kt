package tumble.app.tumble.presentation.views
import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tumble.app.tumble.presentation.views.navigation.BottomNavGraph
import tumble.app.tumble.presentation.views.navigation.TabBar
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(showBackground = true, name = "AppParent preview")
@Composable
fun AppParent() {
    val navController = rememberNavController()
    val navBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackEntry?.destination
    Scaffold(
        bottomBar = {
            TabBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }
    ) {
        BottomNavGraph(navController = navController)
    }
}
