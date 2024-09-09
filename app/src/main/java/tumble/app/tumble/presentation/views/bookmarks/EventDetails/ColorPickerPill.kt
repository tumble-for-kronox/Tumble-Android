package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorPickerPill(openColorPicker: () -> Unit){
    Button(
        onClick = openColorPicker,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onSurface
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(
                imageVector = Icons.Default.Brush,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Color",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}