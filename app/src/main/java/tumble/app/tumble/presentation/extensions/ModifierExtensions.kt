package tumble.app.tumble.presentation.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.borderRadius(size: Dp) = clip(RoundedCornerShape(size))

fun Modifier.searchBox() = padding(10.dp)
    .background(Color.Gray.copy(alpha= 0.3F))
    .borderRadius(10.dp)
    .padding(top = 15.dp, bottom = 10.dp)
    .padding(horizontal = 15.dp)