package tumble.app.tumble.presentation.screens.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val navigationAction: (@Composable () -> Unit)? = null
)