package tumble.app.tumble.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.copyFromRealm
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tumble.app.tumble.data.notifications.NotificationManager
import tumble.app.tumble.data.repository.preferences.CombinedData
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.data.repository.realm.RealmManager
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.domain.enums.Types.AppearanceType
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.presentation.views.Settings.Notifications.NotificationOffset
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val schoolManager: SchoolManager,
    val realmManager: RealmManager,
    val notificationManager: NotificationManager

): ViewModel()  {

    var presentSideBarSheet:Boolean = false

    var authStatus: AccountViewModel.AuthStatus = AccountViewModel.AuthStatus.UNAUTHORIZED

    var authSchoolId: Int = -1

    var schoolName: String = ""

    private val _combinedData = MutableStateFlow(CombinedData())
    val combinedData: StateFlow<CombinedData> = _combinedData

    private val _appearance = MutableStateFlow<AppearanceType>(AppearanceType.AUTOMATIC)
    val appearance: StateFlow<AppearanceType> = _appearance

    private val _notificationOffset = MutableStateFlow<NotificationOffset>(NotificationOffset.Thirty)
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
                _combinedData.value = combinedDat
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

    fun upDateAppearance(type: AppearanceType) {
        viewModelScope.launch {
            dataStoreManager.setAppearance(type)
        }
    }

    fun removeNotifications(id: String, events: List<Event>){
        //TODO
    }

    fun clearAllNotifications(){
        //TODO
    }

    fun rescheduleNotifications(previousOffset: Int, newOffset: NotificationOffset){
        viewModelScope.launch {
            dataStoreManager.setNotificationOffset(newOffset)
        }
    }

    fun scheduleNotificationsForAllEvents(allEvents: List<Event>){
        //TODO
    }

    fun deleteBookmark(schedule: Schedule){
        //TODO
    }

    fun updateBookmarkVisibility(visibility: Boolean, schedule: Schedule){
        viewModelScope.launch {
            realmManager.updateScheduleVisibility(schedule.scheduleId, visibility)
        }
    }

    fun deleteAllSchedules(){
        //TODO
    }

    override fun onCleared() {
        super.onCleared()
        _cancellables.forEach { it.cancel() }
    }
}