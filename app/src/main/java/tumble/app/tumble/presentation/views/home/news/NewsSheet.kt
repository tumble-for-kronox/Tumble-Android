package tumble.app.tumble.presentation.views.home.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NewsItems
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton

@Composable
fun NewsSheet(news: NewsItems?, showSheet: MutableState<Boolean>) {
    var searching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val filteredNews = news?.filter { it.title.contains(searchText, ignoreCase = true) } ?: listOf()

    Column(
        modifier = Modifier.padding(all = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextField(
            placeholder = { Text(text = stringResource(id = R.string.search_news)) },
            value = searchText,
            onValueChange = { searchText = it },
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        filteredNews.forEach { newsItem ->
            NewsItemCard(newsItem = newsItem) { }
        }
    }
    CloseCoverButton {
        showSheet.value = !showSheet.value;
    }
}
