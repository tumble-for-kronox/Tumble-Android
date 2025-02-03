package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tumble.app.tumble.extensions.presentation.noRippleClickable


@Composable
fun BackButton(label: String = "", onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = label,
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(
                start = 10.dp,
                top = 10.dp,
                end = 24.dp,
                bottom = 10.dp
            ),
        tint = Color.White
    )
}