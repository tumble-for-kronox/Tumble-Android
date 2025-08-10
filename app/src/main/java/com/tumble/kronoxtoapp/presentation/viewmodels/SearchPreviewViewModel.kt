package com.tumble.kronoxtoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.other.extensions.models.assignCourseRandomColors
import com.tumble.kronoxtoapp.other.extensions.models.hasNoEvents
import com.tumble.kronoxtoapp.other.extensions.models.hasScheduleWithId
import com.tumble.kronoxtoapp.other.extensions.models.toRealmSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class BookmarkState {
    data object Idle : BookmarkState()
    data object Loading : BookmarkState()
    data class Bookmarked(val isBookmarked: Boolean) : BookmarkState()
    data class Error(val message: String) : BookmarkState()
}

sealed class SearchPreviewState {
    data object Loading : SearchPreviewState()
    data class Loaded(
        val schedule: NetworkResponse.Schedule,
        val colorsForPreview: MutableMap<String, String>,
        val bookmarkState: BookmarkState = BookmarkState.Idle
    ) : SearchPreviewState()
    data object Empty : SearchPreviewState()
    data class Error(val errorMessage: String) : SearchPreviewState()
}

@HiltViewModel
class SearchPreviewViewModel @Inject constructor(
    private val kronoxManager: KronoxService,
    private val realmService: RealmService,
    private val schoolService: SchoolService
): ViewModel() {

    private val _state = MutableStateFlow<SearchPreviewState>(SearchPreviewState.Loading)
    val state: StateFlow<SearchPreviewState> = _state.asStateFlow()

    private val schools by lazy { schoolService.getSchools() }

    private var currentScheduleData: ScheduleData? = null

    data class ScheduleData(
        val schedule: NetworkResponse.Schedule,
        val schoolId: String,
        val colorsForPreview: MutableMap<String, String>,
        val scheduleTitle: String
    )

    fun getSchedule(programmeId: String, schoolId: String, scheduleTitle: String) {
        _state.value = SearchPreviewState.Loading
        viewModelScope.launch {
            try {
                val endpoint = Endpoint.Schedule(scheduleId = programmeId, schoolId = schoolId)
                val fetchedSchedule: ApiResponse<NetworkResponse.Schedule> = kronoxManager.getSchedule(endpoint)
                val parsedSchedule = parseFetchedSchedules(fetchedSchedule)

                updateUIWithFetchedSchedule(
                    parsedSchedule,
                    schoolId,
                    scheduleTitle
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMessage = "An unexpected error occurred. Please try again later."
                    _state.value = SearchPreviewState.Error(e.localizedMessage ?: errorMessage)
                }
            }
        }
    }

    private fun parseFetchedSchedules(schedules: ApiResponse<NetworkResponse.Schedule>): NetworkResponse.Schedule {
        return when(schedules) {
            is ApiResponse.Success -> schedules.data
            is ApiResponse.Error -> throw Exception("Failed to fetch schedule")
        }
    }

    private suspend fun updateUIWithFetchedSchedule(
        fetchedSchedule: NetworkResponse.Schedule,
        schoolId: String,
        scheduleTitle: String
    ) {
        val scheduleId = fetchedSchedule.id

        withContext(Dispatchers.Main) {
            if (fetchedSchedule.hasNoEvents()) {
                _state.value = SearchPreviewState.Empty
                return@withContext
            }

            val bookmarkedSchedules = realmService.getAllSchedules()
            val isBookmarked = bookmarkedSchedules.hasScheduleWithId(scheduleId)

            val colorsForPreview = if (isBookmarked) {
                realmService.getCourseColors()
            } else {
                withContext(Dispatchers.Default) {
                    fetchedSchedule.assignCourseRandomColors()
                }
            }

            currentScheduleData = ScheduleData(
                schedule = fetchedSchedule,
                schoolId = schoolId,
                colorsForPreview = colorsForPreview,
                scheduleTitle = scheduleTitle
            )

            _state.value = SearchPreviewState.Loaded(
                schedule = fetchedSchedule,
                colorsForPreview = colorsForPreview,
                bookmarkState = BookmarkState.Bookmarked(isBookmarked)
            )
        }
    }

    fun toggleBookmark() {
        val currentState = _state.value
        if (currentState !is SearchPreviewState.Loaded) return

        val scheduleData = currentScheduleData ?: return
        val currentBookmarkState = currentState.bookmarkState

        if (currentBookmarkState !is BookmarkState.Bookmarked) return

        _state.value = currentState.copy(
            bookmarkState = BookmarkState.Loading
        )

        viewModelScope.launch {
            try {
                if (currentBookmarkState.isBookmarked) {
                    removeBookmark(scheduleData.schedule.id)
                } else {
                    addBookmark(scheduleData)
                }

                _state.value = currentState.copy(
                    bookmarkState = BookmarkState.Bookmarked(!currentBookmarkState.isBookmarked)
                )

            } catch (e: Exception) {
                _state.value = currentState.copy(
                    bookmarkState = BookmarkState.Error(
                        e.localizedMessage ?: "Failed to update bookmark"
                    )
                )

                kotlinx.coroutines.delay(2000)
                _state.value = currentState.copy(
                    bookmarkState = currentBookmarkState
                )
            }
        }
    }

    private suspend fun removeBookmark(scheduleId: String) {
        val realmSchedule = realmService.getScheduleByScheduleId(scheduleId)
        realmSchedule?.let {
            realmService.deleteSchedule(it)
        }
    }

    private suspend fun addBookmark(scheduleData: ScheduleData) {
        val realmSchedule = scheduleData.schedule.toRealmSchedule(
            scheduleRequiresAuth = scheduleRequiresAuth(scheduleData.schoolId),
            schoolId = scheduleData.schoolId,
            existingCourseColors = scheduleData.colorsForPreview,
            scheduleTitle = scheduleData.scheduleTitle
        )
        realmService.saveSchedule(realmSchedule)
    }

    private fun scheduleRequiresAuth(schoolId: String): Boolean {
        return schools.firstOrNull { it.id == schoolId.toInt() }?.loginRq ?: false
    }

}