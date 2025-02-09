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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun UsernameField(username: MutableState<String>){
    OutlinedTextField(
        value = username.value,
        onValueChange = { value: String -> username.value = value },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Email Icon",
                tint = MaterialTheme.colors.onSurface.copy(.4f)
            )
        },
        placeholder = {
            Text("Username/Email address", color = MaterialTheme.colors.onSurface.copy(.25f))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            autoCorrect = false
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(.4f),
            cursorColor = MaterialTheme.colors.primary,
            focusedTextColor = MaterialTheme.colors.onBackground,
            unfocusedTextColor = MaterialTheme.colors.onBackground
        )
    )
}