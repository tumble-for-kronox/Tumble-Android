package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import tumble.app.tumble.datasource.network.Endpoint
import tumble.app.tumble.datasource.network.kronox.KronoxManager
import tumble.app.tumble.datasource.network.url
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NewsItems
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val kronoxManager: KronoxManager): ViewModel() {
    val newsItems = MutableStateFlow<NewsItems?>(null);

    suspend fun getNews() {
        val response = kronoxManager.get<NetworkResponse>(Endpoint.News.url(), null)

        val bla = response.body();
    }
}