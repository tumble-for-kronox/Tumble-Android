package tumble.app.tumble.presentation.views.Settings.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.extensions.presentation.noRippleClickable

@Composable
fun SettingsRadioButton(
    title: String,
    isSelected: Boolean,
    onValueChange: () -> Unit,
) {

        Row(
            modifier = Modifier
                .noRippleClickable { onValueChange() }
                .fillMaxWidth()
                .padding(7.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .background(MaterialTheme.colors.primary, CircleShape),
                    tint = MaterialTheme.colors.onPrimary
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                            CircleShape
                        )
                )
            }
        }
}