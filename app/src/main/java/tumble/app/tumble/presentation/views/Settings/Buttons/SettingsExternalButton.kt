package tumble.app.tumble.presentation.views.Settings.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.extensions.presentation.noRippleClickable

data class SettingsDetails(
    val titleKey: String,
    val name: String,
    val details: String
)

@Composable
fun SettingsExternalButton(
    title: String,
    current: String? = null,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector = Icons.Default.ArrowOutward,
    leadingIconBackgroundColor: Color = MaterialTheme.colors.onSurface,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                // Trigger haptic feedback here if needed
                action()
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(34.dp)
                .background(color = leadingIconBackgroundColor, shape = RoundedCornerShape(10.dp))
                .padding(6.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        if (current != null) {
            Text(
                text = current,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
        )
    }
}