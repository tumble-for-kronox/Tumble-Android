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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NewsItems

@Composable
fun AllNews(news: NewsItems?) {
    Column(
        modifier = Modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text(
                text = stringResource(R.string.other_news),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier)
        }
        Column(modifier = Modifier.padding(top = 7.5.dp)) {
            if (news != null && news.size >= 4) {
                if (news.drop(4).isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.no_other_news),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onBackground
                    )
                } else {
                    news.drop(4).forEach { newsItem ->
                        NewsItemCard(newsItem = newsItem) { }
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.no_other_news),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}
