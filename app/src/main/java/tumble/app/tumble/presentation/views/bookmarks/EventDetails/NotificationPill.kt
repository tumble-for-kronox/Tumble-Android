package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.presentation.viewmodels.NotificationState
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NotificationPill(
    state: NotificationState,
    title: String,
    image: String,
    onTap: () -> Unit
){
    Button(
        onClick = onTap,
        enabled = state != NotificationState.LOADING,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onSurface
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically){
            when(state){
                NotificationState.SET, NotificationState.NOT_SET -> {
//                    Icon(
//                        painter = painterResource(id = image.toInt()),
//                        contentDescription = null,
//                        modifier = Modifier.size(14.dp),
//                        tint = MaterialTheme.colors.onSurface
//                    )
// TODO("Icon not implemented.")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                NotificationState.LOADING -> {
                    CustomProgressIndicator()
                }
            }
        }
    }
}