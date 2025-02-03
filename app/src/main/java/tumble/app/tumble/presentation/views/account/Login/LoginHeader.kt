package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginHeader(){
    Column(
        modifier = Modifier
            .padding(bottom = 35.dp, top = 45.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Log in",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please log in to continue",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}