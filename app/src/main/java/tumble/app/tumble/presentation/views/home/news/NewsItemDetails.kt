package tumble.app.tumble.presentation.views.home.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.network.NetworkResponse

@Composable
fun NewsItemDetails(newsItem: NetworkResponse.NotificationContent) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = newsItem.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onBackground
        )
    }
}
