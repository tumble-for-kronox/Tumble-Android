package com.tumble.kronoxtoapp.presentation.screens.account.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun UsernameField(username: MutableState<String>) {
    TextField(
        value = username.value,
        onValueChange = { value: String -> username.value = value },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Username Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        placeholder = {
            Text(
                "Username/Email address",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
        ,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            autoCorrectEnabled = false
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun PasswordField(
    password: MutableState<String>,
    visiblePassword: MutableState<Boolean>
) {
    TextField(
        value = password.value,
        onValueChange = { value: String -> password.value = value },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            IconButton(onClick = { visiblePassword.value = !visiblePassword.value }) {
                Icon(
                    imageVector = if (visiblePassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (visiblePassword.value) "Hide password" else "Show password",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        placeholder = {
            Text(
                "Password",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
        ,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            autoCorrectEnabled = false
        ),
        visualTransformation = if (visiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}