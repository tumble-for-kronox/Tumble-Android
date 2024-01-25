package tumble.app.tumble.presentation.views.home.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NewsItems

@Composable
fun RecentNews(news: NewsItems?) {
    Column(
        modifier = Modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text(
                text = stringResource(R.string.recent_news),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        news?.take(4)?.forEach { newsItem ->
            NewsItemCard(newsItem = newsItem) { }
        }
    }
}
