package tumble.app.tumble.presentation.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tumble.app.tumble.data.repository.realm.RealmManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.domain.enums.HomeStatus
import tumble.app.tumble.domain.enums.PageState
import tumble.app.tumble.domain.models.network.NewsItems
import tumble.app.tumble.domain.models.presentation.EventDetailsSheetModel
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.extensions.models.filterEventsMatchingToday
import tumble.app.tumble.extensions.models.findNextUpcomingEvent
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.observables.NetworkController
import tumble.app.tumble.presentation.models.WeekEventCardModel
import tumble.app.tumble.utils.sortedEventOrder
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val kronoxRepository: KronoxRepository,
    private val realmManager: RealmManager,
    private val networkController: NetworkController
) : ViewModel() {

    var newsSectionStatus by mutableStateOf(PageState.LOADING)
    var news: NewsItems? by mutableStateOf(null)
    var swipedCards = mutableIntStateOf(0)
    var status by mutableStateOf<HomeStatus>(HomeStatus.LOADING)
    var todaysEventsCards = mutableStateOf(listOf<WeekEventCardModel>())
    var nextClass: Event? by mutableStateOf(null)

    private val appController = AppController.shared
    private val _cancellables = mutableListOf<Job>()
    private var initializedSession = false
    var eventSheet by mutableStateOf<EventDetailsSheetModel?>(null)

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

    override fun onCleared() {
        super.onCleared()
        _cancellables.forEach { it.cancel() }
    }
}