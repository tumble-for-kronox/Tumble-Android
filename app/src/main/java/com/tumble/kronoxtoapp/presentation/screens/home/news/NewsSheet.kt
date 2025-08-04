package com.tumble.kronoxtoapp.presentation.screens.home.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.NewsItems
import com.tumble.kronoxtoapp.presentation.components.buttons.CloseCoverButton
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@Composable
fun NewsSheet(
    news: NewsItems?,
    onClose: () -> Unit,
    setTopNavState: (AppBarState) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var selectedNewsItem by remember { mutableStateOf<NetworkResponse.NotificationContent?>(null) }

    val filteredNews = remember(news, searchText) {
        news?.filter {
            it.title.contains(searchText, ignoreCase = true) || it.body.contains(searchText, ignoreCase = true)
        } ?: emptyList()
    }

    val title = stringResource(R.string.news_title)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (selectedNewsItem != null) {
            NewsDetails(
                newsItem = selectedNewsItem!!,
                onBackToList = { selectedNewsItem = null },
                onClose = onClose,
                setTopNavState = setTopNavState,
            )
        } else {
            LaunchedEffect(key1 = true) {
                setTopNavState(
                    AppBarState(
                        title = title,
                        actions = {
                            CloseCoverButton {
                                onClose()
                            }
                        }
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                NewsSearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    if (filteredNews.isEmpty() && searchText.isNotEmpty()) {
                        item {
                            Info("No news found")
                        }
                    } else {
                        items(filteredNews) { newsItem ->
                            NewsItemCard(
                                newsItem = newsItem,
                                onClick = {
                                    selectedNewsItem = newsItem
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searching = remember { mutableStateOf(false) }

    DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { searching.value = false },
        active = false,
        onActiveChange = { },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_news),
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f),
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.075f),
            dividerColor = MaterialTheme.colorScheme.primary,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
        ),
    ) {}
}
