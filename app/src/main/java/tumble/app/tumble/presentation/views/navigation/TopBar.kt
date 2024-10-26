package tumble.app.tumble.presentation.views.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(appBarState: AppBarState ) {
    CenterAlignedTopAppBar(
        title = { Text(appBarState.title) },
        actions = { appBarState.actions?.invoke(this) },
        navigationIcon = { appBarState.navigationAction?.invoke() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colors.background,
            titleContentColor = MaterialTheme.colors.onBackground,
            actionIconContentColor = MaterialTheme.colors.primary,
            navigationIconContentColor = MaterialTheme.colors.primary
        )
    )
}