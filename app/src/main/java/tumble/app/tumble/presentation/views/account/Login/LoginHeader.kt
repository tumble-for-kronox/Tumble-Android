package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoginHeader(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Log in")
        
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Please log in to continue")        
    }
}