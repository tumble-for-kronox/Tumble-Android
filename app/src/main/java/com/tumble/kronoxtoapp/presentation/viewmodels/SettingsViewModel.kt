package com.tumble.kronoxtoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.data.repository.preferences.CombinedData
import com.tumble.kronoxtoapp.data.repository.preferences.DataStoreManager
import com.tumble.kronoxtoapp.data.repository.realm.RealmManager
import com.tumble.kronoxtoapp.domain.enums.types.AppearanceType
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.presentation.screens.settings.preferences.notifications.NotificationOffset
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val realmManager: RealmManager,
): ViewModel()  {
    private val _appearance = MutableStateFlow(AppearanceType.AUTOMATIC)
    val appearance: StateFlow<AppearanceType> = _appearance

    private val _notificationOffset = MutableStateFlow(NotificationOffset.Thirty)
    val notificationOffset: StateFlow<NotificationOffset> = _notificationOffset

    private val _bookmarks = MutableStateFlow<List<Schedule>>(mutableListOf())
    val bookmarks: StateFlow<List<Schedule>> = _bookmarks

    private val _cancellables = mutableListOf<Job>()

    init {
        setUpDataPublishers()
    }

    private fun setUpDataPublishers(){

        val job1 = viewModelScope.launch {
            combine(
                dataStoreManager.authSchoolId,
                dataStoreManager.appearance,
                dataStoreManager.notificationOffset
            ){schoolId, appearance, notificationOffset ->
                CombinedData(authSchoolId = schoolId, appearance = appearance, notificationOffset = notificationOffset)
            }.collect { combinedDat ->
                _appearance.value = combinedDat.appearance!!
                _notificationOffset.value = combinedDat.notificationOffset!!
            }
        }

        val job2 = viewModelScope.launch {
            val schedules = realmManager.getAllLiveSchedules().asFlow()
            schedules.collect{ newSchedules ->
                _bookmarks.value = newSchedules.list
            }
        }
        _cancellables.add(job1)
        _cancellables.add(job2)
    }

    fun updateAppearance(type: AppearanceType) {
        viewModelScope.launch {
            dataStoreManager.setAppearance(type)
        }
    }

    fun updateBookmarkVisibility(visibility: Boolean, schedule: Schedule){
        viewModelScope.launch {
            realmManager.updateScheduleVisibility(schedule.scheduleId, visibility)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _cancellables.forEach { it.cancel() }
    }

    fun deleteBookmark(schedule: Schedule) {

    }

    fun rescheduleNotifications(value: Int, type: NotificationOffset) {

    }
}