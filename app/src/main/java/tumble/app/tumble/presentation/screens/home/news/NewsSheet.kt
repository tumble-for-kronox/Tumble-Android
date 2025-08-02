package tumble.app.tumble.presentation.screens.home.news

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NewsItems
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSheet(
    news: NewsItems?,
    sheetState: SheetState,
    showSheet: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    val filteredNews = remember(news, searchText) {
        news?.filter {
            it.title.contains(searchText, ignoreCase = true)
        } ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text(stringResource(id = R.string.search_news)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        filteredNews.forEach { newsItem ->
            NewsItemCard(newsItem = newsItem) {
                // TODO: Handle onClick
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CloseCoverButton {
            scope.launch {
                sheetState.hide()
                showSheet.value = false
            }
        }
    }
}
