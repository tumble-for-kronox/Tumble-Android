package com.tumble.kronoxtoapp.presentation.screens.account.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginButton(
    onLogin: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = {
            if (enabled) {
                onLogin()
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
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