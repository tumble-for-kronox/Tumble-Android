package tumble.app.tumble.presentation.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R

@Composable
fun HomeNotAvailable() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Everything looks good for this week", // Replace with string resource for localization
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 10.dp)
            )

            Text(
                text = "Looks like you don't have any classes or exams in the coming week. Yay!", // Replace with string resource
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 25.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.woman_lounging),
            contentDescription = "woman_lounging",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
