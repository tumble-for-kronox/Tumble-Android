package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun PasswordField(
    password: MutableState<String>,
    visiblePassword: MutableState<Boolean>
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(15.dp))
            .padding(15.dp)
    ){
        Icon(imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(value = password.value,
            onValueChange = {password.value = it},
            placeholder = { Text(text = "Password")},
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (visiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            trailingIcon = {
                IconButton(onClick = { visiblePassword.value = !visiblePassword.value }
                ) {
                    Icon(imageVector = if (visiblePassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.onSurface
            )
        )
    }
}