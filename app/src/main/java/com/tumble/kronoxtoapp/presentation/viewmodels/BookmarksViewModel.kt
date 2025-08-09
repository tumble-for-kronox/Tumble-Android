package com.tumble.kronoxtoapp.presentation.viewmodels

import android.icu.util.Calendar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.domain.enums.BookmarksStatus
import com.tumble.kronoxtoapp.domain.enums.ViewType
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.realm.Day
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.other.extensions.models.filterEmptyDays
import com.tumble.kronoxtoapp.other.extensions.models.filterValidDays
import com.tumble.kronoxtoapp.other.extensions.models.flattenAndMerge
import com.tumble.kronoxtoapp.other.extensions.models.groupByWeek
import com.tumble.kronoxtoapp.other.extensions.models.isMissingEvents
import com.tumble.kronoxtoapp.other.extensions.models.ordered
import com.tumble.kronoxtoapp.utils.preprocessDateString
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

data class BookmarkData(
    val days: List<Day>,
    val calendarEventsByDate: Map<LocalDate, List<Event>>,
    val weeks: Map<Int, List<Day>>,
    val weekStartDates: List<Date>
)

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val realmService: RealmService,
    private val dataStoreService: DataStoreService,
): ViewModel(){

    private val _cancellables = mutableListOf<Job>()

    var status by mutableStateOf<BookmarksStatus>(BookmarksStatus.LOADING)
    var bookmarkData by mutableStateOf(BookmarkData(listOf(), mutableMapOf(), mapOf(), listOf()))

    private val _defaultViewType = MutableStateFlow(ViewType.LIST)
    val defaultViewType: StateFlow<ViewType> = _defaultViewType

    var selectedDate by mutableStateOf<LocalDate>(LocalDate.now())
    var todaysDate by mutableStateOf<LocalDate>(LocalDate.now())
    var eventSheet by mutableStateOf<EventDetailsSheetModel?>(null)

    init {
        setupFlows()
    }

    private fun setupFlows(){

        val job = viewModelScope.launch {
            setupRealmListener()
        }
        val job2 = viewModelScope.launch {
            dataStoreService.viewType.collect { viewType ->
                _defaultViewType.value = viewType
            }
        }

        _cancellables.add(job)
        _cancellables.add(job2)
    }

    private suspend fun setupRealmListener(){
        val schedules = realmService.getAllLiveSchedules().asFlow()
        schedules.collect{ newSchedules ->
            createDaysAndCalenderEvents(newSchedules.list)
        }
    }

    private fun createDaysAndCalenderEvents(schedules: List<Schedule>){

        status = BookmarksStatus.LOADING
        val hiddenScheduleIds = extractHiddenScheduleIds(schedules)
        val visibleSchedules = extractVisibleSchedules(schedules)

        if (schedules.isEmpty()){
            status = BookmarksStatus.UNINITIALIZED
            return
        }

        if (areAllSchedulesHidden(schedules)){
            status = BookmarksStatus.HIDDEN_ALL
            return
        }

        if (areVisibleSchedulesEmpty(visibleSchedules)){
            status = BookmarksStatus.EMPTY
            return
        }

        val days = processSchedules(schedules, hiddenScheduleIds)
        val calendarEvents = makeCalendarEvents(days)
        updateView(days, calendarEvents)
    }

    fun setViewType(viewType: ViewType){
        _defaultViewType.value = viewType
        viewModelScope.launch {
            dataStoreService.setBookmarksViewType(viewType)
        }
    }

    // Helper Functions
    private fun extractHiddenScheduleIds(schedules: List<Schedule>): List<String> {
        return schedules.filter{ !it.toggled}.map { it.scheduleId }
    }

    private fun extractVisibleSchedules(schedules: List<Schedule>): List<Schedule> {
        return schedules.filter { it.toggled }
    }

    private fun areAllSchedulesHidden(schedules: List<Schedule>): Boolean {
        return schedules.all { !it.toggled }
    }

    private fun areVisibleSchedulesEmpty(visibleSchedules: List<Schedule>): Boolean {
        return visibleSchedules.all { it.isMissingEvents()}
    }

    private fun processSchedules(schedules: List<Schedule>, hiddenScheduleIds: List<String>): List<Day> {
        return filterHiddenBookmarks(schedules, hiddenScheduleIds)
            .flattenAndMerge()
            .ordered()
            .filterEmptyDays()
            .filterValidDays()
    }

    private fun filterHiddenBookmarks(schedules: List<Schedule>, hiddenScheduleIds: List<String>): List<Schedule> {
        return schedules.filter { it.scheduleId !in hiddenScheduleIds}
    }

    private fun makeCalendarEvents(days: List<Day>): Map<LocalDate, List<Event>> {
        val dict: MutableMap<LocalDate, List<Event>> = mutableMapOf()
        for (day in days){
            val date = LocalDate.parse(day.isoString?.substring(0,10)
                ?.let { preprocessDateString(it) })
            for (event in day.events!!){

                if (dict[date] == null){
                    dict[date] = mutableListOf(event)
                }else{
                    dict[date] = dict[date]!! + event
                }
            }
        }
        return dict.toMap()
    }

    private fun updateView(days: List<Day>, calendarEvents: Map<LocalDate, List<Event>>) {
        bookmarkData = BookmarkData(
            days = days,
            calendarEventsByDate = calendarEvents,
            weeks = days.groupByWeek(),
            weekStartDates = weekStartDates()
        )
        status = BookmarksStatus.LOADED
    }

    fun updateSelectedDate(clickedDate: LocalDate){
        selectedDate = if (selectedDate != clickedDate){
            clickedDate
        } else {
            LocalDate.now()
        }
    }

    private fun weekStartDates(): List<Date> {
        val calendar = Calendar.getInstance()
        val result: MutableList<Date> = mutableListOf()

        val currentWeekday = calendar.get(Calendar.DAY_OF_WEEK)
        val daysToSubtract = (currentWeekday + 5) % 7
        calendar.add(Calendar.DAY_OF_WEEK, -daysToSubtract)
        val nearestMonday = calendar.time

        var currentDate = nearestMonday
        calendar.add(Calendar.MONTH, 6)
        val endDate = calendar.time

        calendar.apply { time = currentDate }

        while (currentDate < endDate){
            result.add(currentDate)
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            currentDate = calendar.time
        }
        return result.toList()
    }

    override fun onCleared() {
        super.onCleared()
        _cancellables.forEach{ it.cancel() }
    }
}