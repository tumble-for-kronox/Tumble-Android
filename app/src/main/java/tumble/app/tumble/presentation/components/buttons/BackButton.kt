package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tumble.app.tumble.presentation.components.Modifiers.clickableWithoutRipple


@Composable
fun BackButton(onClick: () -> Unit, label: String) {
    Row(
        modifier = Modifier.clickableWithoutRipple { onClick() }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
        Text(
            text = label,
            color = MaterialTheme.colors.primary
        )
    }
}