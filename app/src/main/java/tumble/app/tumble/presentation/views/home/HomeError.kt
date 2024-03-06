package tumble.app.tumble.presentation.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeError() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Something went wrong", // Replace with string resource for localization
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground
            )

            Text(
                text = "We experienced an issue when trying to find your schedules. Try again later", // Replace with string resource
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = Icons.Default.Warning,
//            painter = painterResource(id = R.drawable.woman_arms_up), // Replace with your drawable resource
            contentDescription = "woman_arms_up", // Provide an appropriate content description
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
