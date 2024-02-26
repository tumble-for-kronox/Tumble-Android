package tumble.app.tumble.presentation.views.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.presentation.viewmodels.HomeViewModel
import tumble.app.tumble.presentation.viewmodels.NewsStatus
import tumble.app.tumble.presentation.viewmodels.Status
import tumble.app.tumble.presentation.views.home.news.News

@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel()) {
    val newsItems by viewModel.newsItems.collectAsState()
    val viewModelStatus by viewModel.status.collectAsState()
    val newsSectionStatus by viewModel.newsStatus.collectAsState();
    val showSheet = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { /* ... Top bar content ... */ },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .background(MaterialTheme.colors.background)
                    .padding(it)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                when (viewModelStatus) {
                    Status.AVAILABLE -> {
                        when (newsSectionStatus) {
                            NewsStatus.LOADING -> Text(text = "LOADING")
                            NewsStatus.LOADED -> Text(text = "LOADED")
                            NewsStatus.FAILED -> Text(text = "FAILED")
                        }
                    }
                    Status.LOADING -> {
                        // CustomProgressIndicator Composable
                    }
                    Status.NO_BOOKMARKS -> {

                    }
                    Status.NOT_AVAILABLE -> {

                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )

    if (showSheet.value) {
        // NewsSheet Composable
    }
}