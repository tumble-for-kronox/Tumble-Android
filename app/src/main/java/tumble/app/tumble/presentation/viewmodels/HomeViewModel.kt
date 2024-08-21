package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.domain.enums.HomeStatus
import tumble.app.tumble.domain.models.network.NewsItems
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val kronoxRepository: KronoxRepository): ViewModel() {
    val status = MutableStateFlow(HomeStatus.AVAILABLE)
    val newsSectionStatus = MutableStateFlow(NewsStatus.LOADING)
    val newsItems = MutableStateFlow<NewsItems?>(null);
    val newsError = MutableStateFlow<Int?>(null)

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            when (val result = kronoxRepository.getNews()) {
                is ApiResponse.Success -> {
                    newsSectionStatus.value = NewsStatus.LOADED
                    newsItems.value = result.data
                    newsError.value = null
                }

                is ApiResponse.Error -> {
                    newsSectionStatus.value = NewsStatus.FAILED
                    newsItems.value = null
                    newsError.value = result.errorCode
                }
                is ApiResponse.Loading -> {
                    newsSectionStatus.value = NewsStatus.LOADING
                    newsItems.value = null
                    newsError.value = null
                }
            }
        }
    }
}

enum class NewsStatus {
    LOADING, LOADED, FAILED
}