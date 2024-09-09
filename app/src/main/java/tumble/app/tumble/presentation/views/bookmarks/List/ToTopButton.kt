package tumble.app.tumble.presentation.views.bookmarks.List

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToTopButton( onClick: () -> Unit){
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(bottom = 40.dp),
        shape = CircleShape
    ){
        Icon(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}