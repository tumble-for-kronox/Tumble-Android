package tumble.app.tumble.presentation.views.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.presentation.viewmodels.HomeViewModel

@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel()) {
    val newsItems by viewModel.newsItems.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNews();
    }

    Text("Showing home")
}