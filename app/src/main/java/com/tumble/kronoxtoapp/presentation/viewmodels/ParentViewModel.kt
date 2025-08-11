package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.enums.types.AppearanceType
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.domain.models.util.parseIsoToInstant
import com.tumble.kronoxtoapp.other.extensions.models.toRealmSchedule
import com.tumble.kronoxtoapp.services.CombinedData
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.RealmService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.isValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ParentViewModel @Inject constructor(
    private val realmService: RealmService,
    private val kronoxRepository: KronoxService,
    private val dataStoreService: DataStoreService,
) : ViewModel() {

    private val _combinedData = MutableStateFlow(CombinedData(-1))
    private val _appearance = MutableStateFlow<AppearanceType>(AppearanceType.AUTOMATIC)
    val appearance: StateFlow<AppearanceType> = _appearance
    private var updateAttempted = false
    private var isUpdatingBookMarks = false

    init {
        observeDataStoreChanges()
    }

    private fun updateRealmSchedules() {
        if (isUpdatingBookMarks) {
            Log.d("updateRealmSchedules", "Update already in progress, skipping...")
            return
        }

        isUpdatingBookMarks = true

        val schedules = realmService.getAllLiveSchedules()

        if (schedules.isNotEmpty()) {
            val scheduleIds = schedules
                .filter { it.isValid() }
                .map { it.scheduleId }

            if (scheduleIds.isNotEmpty()) {
                viewModelScope.launch {
                    try {
                        updateBookmarks(scheduleIds)
                        withContext(Dispatchers.Main) {
                            isUpdatingBookMarks = false
                            dataStoreService.setLastUpdated(Instant.now())
                            updateAttempted = true
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            isUpdatingBookMarks = false
                            Log.e(
                                "updateRealmSchedules",
                                "Failed to update schedules: ${e.localizedMessage}"
                            )
                        }
                    }
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    isUpdatingBookMarks = false
                    dataStoreService.setLastUpdated(Instant.now())
                    updateAttempted = true
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                isUpdatingBookMarks = false
                dataStoreService.setLastUpdated(Instant.now())
                updateAttempted = true
            }
        }
    }

    private suspend fun updateBookmarks(scheduleIds: List<String>) {
        var updatedSchedules = 0
        val scheduleCount = scheduleIds.size

        for (scheduleId in scheduleIds) {
            try {
                fetchAndUpdateSchedule(scheduleId)
                updatedSchedules++
            } catch (e: Exception) {
                Log.e(
                    "updateBookmarks",
                    "Failed to update schedule $scheduleId: ${e.localizedMessage}"
                )
            }
        }

        if (updatedSchedules != scheduleCount) {
            Log.w("updateBookmarks", "Not all schedules were updated successfully")
        }

        Log.d("updateBookmarks", "Finished updating schedules")
    }

    private suspend fun fetchAndUpdateSchedule(scheduleId: String) {
        val schedule = realmService.getScheduleByScheduleId(scheduleId)
            ?: throw Exception("Schedule not found: $scheduleId")

        if (!schedule.isValid()) {
            throw Exception("Schedule is invalidated: $scheduleId")
        }

        try {
            val endpoint =
                Endpoint.Schedule(scheduleId = schedule.scheduleId, schoolId = schedule.schoolId)
            val fetchedScheduleResponse: ApiResponse<NetworkResponse.Schedule> =
                kronoxRepository.getSchedule(endpoint)

            when (fetchedScheduleResponse) {
                is ApiResponse.Success -> {
                    Log.i(
                        "fetchAndUpdateSchedule",
                        "Fetched new schedule with id $scheduleId. Will attempt to update"
                    )
                    updateSchedule(
                        schedule = fetchedScheduleResponse.data,
                        schoolId = schedule.schoolId,
                        existingSchedule = schedule
                    )
                }

                is ApiResponse.Error -> {
                    throw Exception("Network error: ${fetchedScheduleResponse.errorMessage}")
                }
            }
        } catch (e: Exception) {
            if (!schedule.isValid()) {
                throw Exception("Schedule invalidated during update: $scheduleId")
            } else {
                throw Exception("Network error: ${e.localizedMessage}")
            }
        }
    }

    private fun updateSchedule(
        schedule: NetworkResponse.Schedule,
        schoolId: String,
        existingSchedule: Schedule
    ) {
        viewModelScope.launch {
            val scheduleRequiresAuth = existingSchedule.requiresAuth
            val scheduleTitle = getScheduleTitle(schedule = existingSchedule, schoolId)
            val realmSchedule: Schedule = schedule.toRealmSchedule(
                scheduleRequiresAuth,
                schoolId,
                existingCourseColors = realmService.getCourseColors(),
                scheduleTitle
            )
            realmService.updateSchedule(scheduleId = schedule.id, newSchedule = realmSchedule)
        }
    }

    private suspend fun getScheduleTitle(schedule: Schedule, schoolId: String): String {
        val scheduleId = schedule.scheduleId
            .replace("p.", "")
            .replace("k.", "")

        try {
            val endpoint = Endpoint.SearchProgramme(searchQuery = scheduleId, schoolId)
            val searchResult: ApiResponse<NetworkResponse.Search> =
                kronoxRepository.getProgramme(endpoint)

            when (searchResult) {
                is ApiResponse.Success -> {
                    val searchResults = searchResult.data.items
                    return searchResults.first().subtitle
                }

                is ApiResponse.Error -> {
                    return ""
                }
            }
        } catch (e: Exception) {
            Log.e("ParentViewModel", "Failed to get schedule title: ${e.localizedMessage}")
            return ""
        }
    }

    private fun updateShouldOccur(): Boolean {
        val lastUpdated = dataStoreService.lastUpdated.value
        val isoLastUpdated = parseIsoToInstant(lastUpdated)
        Log.d("ParentViewModel", "Schedules were last updated at $isoLastUpdated")
        val threeHoursAgo = Instant.now().minusSeconds(3 * 60 * 60)
        return isoLastUpdated.isBefore(threeHoursAgo)
    }

    private fun observeDataStoreChanges() {
        viewModelScope.launch {
            dataStoreService.authSchoolId.combine(dataStoreService.userOnBoarded) { authSchoolId, userOnBoarded ->
                CombinedData(authSchoolId = authSchoolId, userOnBoarded = userOnBoarded)
            }.collect { combinedData ->
                _combinedData.value = combinedData
            }
        }

        viewModelScope.launch {
            dataStoreService.isInitialized.collect { isInitialized ->
                if (isInitialized && updateShouldOccur() && !updateAttempted) {
                    Log.d("ParentViewModel", "DataStore initialized - checking for updates...")
                    updateRealmSchedules()
                }
            }
        }

        viewModelScope.launch {
            dataStoreService.appearance.collect {
                _appearance.value = it
            }
        }
    }
}
