package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.Endpoint
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.datasource.network.url
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.NewsItems
import tumble.app.tumble.presentation.errors.NewsApiErrorMapper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val kronoxRepository: KronoxRepository): ViewModel() {
    val status = MutableStateFlow(Status.AVAILABLE)
    val newsStatus = MutableStateFlow(NewsStatus.LOADING)
    val newsItems = MutableStateFlow<NewsItems?>(null);
    val newsError = MutableStateFlow<Int?>(null)

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            when (val result = kronoxRepository.getNews()) {
                is ApiResponse.Success -> {
                    newsStatus.value = NewsStatus.LOADED
                    newsItems.value = result.data
                    newsError.value = null
                }

                is ApiResponse.Error -> {
                    newsStatus.value = NewsStatus.FAILED
                    newsItems.value = null
                    newsError.value = result.errorCode
                }
                is ApiResponse.Loading -> {
                    newsStatus.value = NewsStatus.LOADING
                    newsItems.value = null
                    newsError.value = null
                }
            }
        }
    }
}

enum class Status {
    LOADING, AVAILABLE, NO_BOOKMARKS, NOT_AVAILABLE
}

enum class NewsStatus {
    LOADING, LOADED, FAILED
}