package tumble.app.tumble.presentation.screens.bookmarks.EventDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPill(
    state: NotificationState,
    title: String,
    onTap: () -> Unit
){
    FilledTonalButton(
        onClick = onTap,
        modifier = Modifier.semantics {
            contentDescription = "Open color picker to change event color"
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row (verticalAlignment = Alignment.CenterVertically){
            when(state){
                NotificationState.SET, NotificationState.NOT_SET -> {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(7.5.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                NotificationState.LOADING -> {
                    CustomProgressIndicator()
                }
            }
        }
    }
}