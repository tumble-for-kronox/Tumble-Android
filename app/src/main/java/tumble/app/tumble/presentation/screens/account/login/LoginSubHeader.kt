package tumble.app.tumble.presentation.screens.account.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun LoginSubHeader(onHelpClick: (Int) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = "User your uni")

        ClickableText(
            text = AnnotatedString("need help"),
            onClick = onHelpClick)
    }
}