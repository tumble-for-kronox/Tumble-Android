package tumble.app.tumble.presentation.views.Settings.Buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.extensions.presentation.noRippleClickable

@Composable
fun SettingsNavigationButton(
    title: String,
    current: String? = null,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    action: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                // Trigger haptic feedback here if needed
                action()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(28.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface
            )
            if (current != null) {
                Text(
                    text = current,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
            )
        }
    }
}