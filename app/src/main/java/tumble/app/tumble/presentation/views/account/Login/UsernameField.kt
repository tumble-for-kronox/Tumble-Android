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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun UsernameField(username: MutableState<String>){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom =  10.dp)
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(15.dp))
            .padding(15.dp)
    ){
        Icon(imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(value = username.value,
            onValueChange = {username.value = it},
            placeholder = {
                Text(text = "Usernam/Email address")
            },
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            visualTransformation = VisualTransformation.None
        )
    }
}