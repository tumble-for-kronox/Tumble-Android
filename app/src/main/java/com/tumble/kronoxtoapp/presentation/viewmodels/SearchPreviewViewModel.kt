package com.tumble.kronoxtoapp.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.tumble.kronoxtoapp.domain.enums.ButtonState
import com.tumble.kronoxtoapp.domain.enums.SheetStatus
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.realm.Schedule
import com.tumble.kronoxtoapp.other.extensions.models.assignCourseRandomColors
import com.tumble.kronoxtoapp.other.extensions.models.toRealmSchedule

@HiltViewModel
class SearchPreviewViewModel @Inject constructor(
    private val kronoxManager: KronoxService,
    private val realmService: RealmService,
    private val schoolService: SchoolService
): ViewModel() {
    private var isSaved by mutableStateOf(false)
    var status by mutableStateOf<SheetStatus>(SheetStatus.LOADING)
    private var errorMessage by mutableStateOf("")
    var buttonState by mutableStateOf<ButtonState>(ButtonState.LOADING)
    var courseColorsForPreview by mutableStateOf<Map<String,String>>(emptyMap())

    var schedule by mutableStateOf<NetworkResponse.Schedule?>(null)

    private val schools by lazy { schoolService.getSchools() }
    private var currentSchedules: List<Schedule> = realmService.getAllSchedules()

    
    fun getSchedule(programmeId: String, schoolId: String){
        val isScheduleSaved = checkSavedSchedule(programmeId = programmeId, schedules = currentSchedules)

        with(Dispatchers.Main){
            status = SheetStatus.LOADING
            isSaved = isScheduleSaved
        }

        viewModelScope.launch {
            try {
                val parsedSchedule: NetworkResponse.Schedule
                val endpoint = Endpoint.Schedule(scheduleId = programmeId, schoolId = schoolId)
                val fetchedSchedule: ApiResponse<NetworkResponse.Schedule> = kronoxManager.getSchedule(endpoint)
                parsedSchedule = parseFetchedSchedules(fetchedSchedule)
                updateUIWithFetchedSchedule(parsedSchedule, existingSchedule = currentSchedules)
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    status = SheetStatus.ERROR
                    errorMessage = "An unexpected error occurred. Please try again later."
                }
            }
        }
    }

    private fun parseFetchedSchedules(schedules: ApiResponse<NetworkResponse.Schedule>): NetworkResponse.Schedule {
        when(schedules){
            is ApiResponse.Success ->{
                return schedules.data
            }
            is ApiResponse.Error -> {
                throw Exception()
            }
            else -> {
                throw Exception()
            }
        }
    }

    
    private fun updateUIWithFetchedSchedule(fetchedSchedule: NetworkResponse.Schedule, existingSchedule: List<Schedule>){
        viewModelScope.launch(Dispatchers.Main) {
            if (!fetchedSchedule.days.any { it.events.isNotEmpty() }){
                status = SheetStatus.EMPTY
                buttonState = ButtonState.DISABLED
            }else if (existingSchedule.map { it.scheduleId }.contains(fetchedSchedule.id)){
                isSaved = true
                buttonState = ButtonState.SAVED
                schedule = fetchedSchedule
                courseColorsForPreview = realmService.getCourseColors()
                status = SheetStatus.LOADED
            } else{
                withContext(Dispatchers.Default){
                    val randomCourseColor = fetchedSchedule.assignCourseRandomColors()
                    withContext(Dispatchers.Main){
                        courseColorsForPreview = randomCourseColor
                        schedule = fetchedSchedule
                        buttonState = ButtonState.NOT_SAVED
                        status = SheetStatus.LOADED
                    }
                }
            }
        }
    }

    private fun scheduleRequiresAuth(scheduleId: String): Boolean {
        return schools.firstOrNull{ it.id == scheduleId.toInt() }?.loginRq ?: false
    }

    fun bookmark(
        scheduleId: String,
        schoolId: String,
        scheduleTitle: String
    ){
        buttonState = ButtonState.LOADING
        viewModelScope.launch {
            if (!isSaved) {
                schedule?.toRealmSchedule(scheduleRequiresAuth(schoolId), schoolId, courseColorsForPreview, scheduleTitle)
                    ?.let { realmSchedule ->
                        realmService.saveSchedule(realmSchedule)
                        isSaved = true
                        buttonState = ButtonState.SAVED
                    }
            } else {
                val realmSchedule = realmService.getScheduleByScheduleId(scheduleId)
                realmSchedule?.let {
                    realmService.deleteSchedule(it)
                    isSaved = false
                    buttonState = ButtonState.NOT_SAVED
                }
            }
        }
    }
    private fun checkSavedSchedule(programmeId: String, schedules: List<Schedule>): Boolean{
        return schedules.map { it.scheduleId }.contains(programmeId)
    }
}