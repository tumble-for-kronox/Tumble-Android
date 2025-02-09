package tumble.app.tumble.presentation.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pending
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R

@Composable
fun HomeNoBookmarks() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Looks like you don't have anything saved yet", // Replace with string resource
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(bottom = 20.dp, end = 10.dp)
            )

            Text(
                text = "Schedules are bookmarked from the search page, which you can access from the search tab.", // Replace with string resource
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 30.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.man_waiting),
            contentDescription = "man_waiting",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
