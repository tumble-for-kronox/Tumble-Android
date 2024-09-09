package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.jvm.internal.impl.types.checker.TypeRefinementSupport.Enabled

@Composable
fun LoginButton(
    login: () -> Unit,
    enabled: Boolean
){
    Button(
        onClick =  login ,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp)
    ) {
        Text(
            text = "Log in",
            style = MaterialTheme.typography.button
        )
    }
}