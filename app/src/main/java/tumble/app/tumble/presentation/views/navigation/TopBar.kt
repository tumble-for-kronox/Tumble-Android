package tumble.app.tumble.presentation.views.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(appBarState: AppBarState ) {
    TopAppBar(
        title = {
            Text(
                text = appBarState.title,
                style = TextStyle(
                    fontSize = 20.sp
                ),
                color = Color.White,
                maxLines = 1
            )
        },
        actions = { appBarState.actions?.invoke(this) },
        navigationIcon = { appBarState.navigationAction?.invoke() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colors.primary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}