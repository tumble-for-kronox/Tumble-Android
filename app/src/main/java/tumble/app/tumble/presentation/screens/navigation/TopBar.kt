package tumble.app.tumble.presentation.screens.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(appBarState: AppBarState) {
    TopAppBar(
        title = {
            Text(
                text = appBarState.title,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                maxLines = 1
            )
        },
        actions = { appBarState.actions?.invoke(this) },
        navigationIcon = { appBarState.navigationAction?.invoke() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}