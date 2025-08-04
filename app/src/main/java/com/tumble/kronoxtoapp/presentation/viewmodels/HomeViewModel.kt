package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.data.repository.realm.RealmManager
import com.tumble.kronoxtoapp.data.api.ApiResponse
import com.tumble.kronoxtoapp.datasource.network.kronox.KronoxRepository
import com.tumble.kronoxtoapp.domain.enums.HomeStatus
import com.tumble.kronoxtoapp.domain.enums.PageState
import com.tumble.kronoxtoapp.domain.models.network.NewsItems
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.extensions.models.filterEventsMatchingToday
import com.tumble.kronoxtoapp.extensions.models.findNextUpcomingEvent
import com.tumble.kronoxtoapp.presentation.models.WeekEventCardModel
import com.tumble.kronoxtoapp.utils.sortedEventOrder
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val kronoxRepository: KronoxRepository,
    private val realmManager: RealmManager,
) : ViewModel() {

    var newsSectionStatus by mutableStateOf(PageState.LOADING)
    var news: NewsItems? by mutableStateOf(null)
    var swipedCards = mutableIntStateOf(0)
    var status by mutableStateOf<HomeStatus>(HomeStatus.LOADING)
    var todaysEventsCards = mutableStateOf(listOf<WeekEventCardModel>())
    var nextClass: Event? by mutableStateOf(null)

    private val _cancellables = mutableListOf<Job>()
    private var initializedSession = false
    var eventSheet by mutableStateOf<EventDetailsSheetModel?>(null)
    var showNewsSheet by mutableStateOf(false)

    init {
        fetchNews()
        viewModelScope.launch {
            setupRealmListener()
        }
    }

    private suspend fun setupRealmListener() {
        val schedules = realmManager.getAllLiveSchedules().asFlow()
        schedules.collect { newSchedules ->
            createCarouselCards(newSchedules.list)
            findNextUpcomingEvent(newSchedules.list)
        }
    }


    private fun fetchNews() {
        Log.d("HomeViewModel", "Fetching news items ...")
        viewModelScope.launch {
            newsSectionStatus = PageState.LOADING

            when (val result = kronoxRepository.getNews()) {
                is ApiResponse.Success -> {
                    news = result.data
                    newsSectionStatus = PageState.LOADED
                }
                is ApiResponse.Error -> {
                    news = null
                    newsSectionStatus = PageState.ERROR
                    Log.e("HomeViewModel", "Failed when fetching news items: ${result.errorMessage}")
                }
                else -> {
                    news = null
                    newsSectionStatus = PageState.ERROR
                    Log.e("HomeViewModel", "Failed when fetching news items: 'else' branch.")
                }
            }

            initializedSession = true
        }
    }


    private fun findNextUpcomingEvent(schedules: List<Schedule>) {
        viewModelScope.launch {
            if (schedules.isEmpty()) {
                status = HomeStatus.NO_BOOKMARKS
            } else if (schedules.none { it.toggled }) {
                status= HomeStatus.NOT_AVAILABLE
            } else {
                val nextUpcomingEvent = schedules.findNextUpcomingEvent()
                nextClass = nextUpcomingEvent
                status = HomeStatus.AVAILABLE
            }
        }
    }

    private fun createCarouselCards(schedules: List<Schedule>) {
        if (schedules.isEmpty() || schedules.none { it.toggled }) return

        viewModelScope.launch {
            status = HomeStatus.LOADING
            val eventsForToday = schedules.filterEventsMatchingToday()
            val todaysEventsCards =
                eventsForToday.sortedWith { a, b -> sortedEventOrder(a, b) }.map {
                    WeekEventCardModel(it)
                }
            todaysEventsCards.let {

            }
        }
    }

    fun changeCourseColor(color: String, courseId: String) {
        viewModelScope.launch {
            realmManager.updateCourseColors(
                courseId,
                color
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        _cancellables.forEach { it.cancel() }
    }
}