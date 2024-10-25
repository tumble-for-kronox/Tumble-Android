package tumble.app.tumble.presentation.components.sheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton


@Composable
fun SheetHeader(
    title: String,
    onClose: () -> Unit
){

    Box(contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = title,// make string resource
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            CloseCoverButton {
                onClose()
            }
        }
    }

}
