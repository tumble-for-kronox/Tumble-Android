package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.network.NewsItems
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.other.extensions.models.findNextUpcomingEvent
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeState {
    data class BookmarksAvailable(val nextClass: Event?) : HomeState()
    data object AllBookmarksHidden : HomeState()
    data object NoBookmarks : HomeState()
    data object Loading : HomeState()
    data class Error(val errorMessage: String) : HomeState()
}

sealed class NewsState {
    data object Loading : NewsState()
    data class Error(val errorMessage: String) : NewsState()
    data class Loaded(val news: NewsItems) : NewsState()
}

data class HomeUiState(
    val showNewsSheet: Boolean = false,
    val eventSheet: EventDetailsSheetModel? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val kronoxRepository: KronoxService,
    private val realmService: RealmService,
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _cancellables = mutableListOf<Job>()
    private var realmListenerJob: Job? = null
    private var initializedSession = false

    init {
        fetchNews()
        setupRealmListener()
    }

    private fun setupRealmListener() {
        realmListenerJob?.cancel()
        realmListenerJob = viewModelScope.launch {
            val schedules = realmService.getAllLiveSchedules().asFlow()
            schedules.collect { newSchedules ->
                findNextUpcomingEvent(newSchedules.list)
            }
        }
    }

    private fun fetchNews() {
        Log.d("HomeViewModel", "Fetching news items ...")
        val job = viewModelScope.launch {
            _newsState.value = NewsState.Loading

            when (val result = kronoxRepository.getNews()) {
                is ApiResponse.Success -> {
                    _newsState.value = NewsState.Loaded(result.data)
                    Log.d("HomeViewModel", "Successfully fetched ${result.data.size} news items")
                }

                is ApiResponse.Error -> {
                    _newsState.value = NewsState.Error(result.errorMessage)
                    Log.e(
                        "HomeViewModel",
                        "Failed when fetching news items: ${result.errorMessage}"
                    )
                }
            }

            initializedSession = true
        }
        _cancellables.add(job)
    }

    private fun findNextUpcomingEvent(schedules: List<Schedule>) {
        val job = viewModelScope.launch {
            _homeState.value = when {
                schedules.isEmpty() -> HomeState.NoBookmarks
                schedules.none { it.toggled } -> HomeState.AllBookmarksHidden
                else -> {
                    val nextUpcomingEvent = schedules.findNextUpcomingEvent()
                    HomeState.BookmarksAvailable(nextUpcomingEvent)
                }
            }
        }
        _cancellables.add(job)
    }

    fun toggleNewsSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showNewsSheet = show)
    }

    fun setEventSheet(eventSheet: EventDetailsSheetModel?) {
        _uiState.value = _uiState.value.copy(eventSheet = eventSheet)
    }

    fun refreshNews() {
        fetchNews()
    }

    override fun onCleared() {
        super.onCleared()
        realmListenerJob?.cancel()
        _cancellables.forEach { it.cancel() }
        _cancellables.clear()
    }
}