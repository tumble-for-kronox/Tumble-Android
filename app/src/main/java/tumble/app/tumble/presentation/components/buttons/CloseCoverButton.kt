package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CloseCoverButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "close",
            modifier = Modifier
                .background(MaterialTheme.colors.onBackground)
                .size(24.dp)
        )
    }
}