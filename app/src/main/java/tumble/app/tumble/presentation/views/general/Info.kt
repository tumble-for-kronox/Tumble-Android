package tumble.app.tumble.presentation.views.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Info(title: String, image: Int?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Replace with your desired background color
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }
        Text(
            text = title,
            textAlign = TextAlign.Center,
            // Add your desired text style
        )
    }
}