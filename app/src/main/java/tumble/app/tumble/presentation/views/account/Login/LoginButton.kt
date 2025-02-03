package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.reflect.jvm.internal.impl.types.checker.TypeRefinementSupport.Enabled

@Composable
fun LoginButton(
    login: () -> Unit,
    enabled: Boolean
){
    Button(
        onClick = {
            if (enabled) {
                login()
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
    ) {
        Text(
            text = "Log in",
            style = TextStyle(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}