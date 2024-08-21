package tumble.app.tumble.presentation.viewmodels

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.datasource.realm.RealmManager
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.Endpoint
import tumble.app.tumble.domain.enums.ButtonState
import tumble.app.tumble.domain.enums.SchedulePreviewStatus
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.extensions.models.assignCourseRandomColors
import tumble.app.tumble.extensions.models.toRealmSchedule
import tumble.app.tumble.utils.isoDateFormatter
import tumble.app.tumble.utils.isoDateFormatterDate
import tumble.app.tumble.utils.toIsoString
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@HiltViewModel
class SearchPreviewViewModel @Inject constructor(
    private val kronoxManager: KronoxRepository,
    private val realmManager: RealmManager,
    private val schoolManager: SchoolManager
): ViewModel() {
    var isSaved by mutableStateOf(false)
    var status by mutableStateOf<SchedulePreviewStatus>(SchedulePreviewStatus.LOADING)
    var errorMessage by mutableStateOf("")
    var buttonState by mutableStateOf<ButtonState>(ButtonState.LOADING)
    var courseColorsForPreview by mutableStateOf<Map<String,String>>(emptyMap())

    var schedule by mutableStateOf<NetworkResponse.Schedule?>(null)

    private val schools by lazy { schoolManager.getSchools() }
    private var currentSchedules: List<Schedule> = realmManager.getAllSchedules()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSchedule(programmeId: String, schoolId: String){
        val isScheduleSaved = checkSavedSchedule(programmeId = programmeId, schedules = currentSchedules)

        with(Dispatchers.Main){
            status = SchedulePreviewStatus.LOADING
            isSaved = isScheduleSaved
        }

        viewModelScope.launch {
            try {
                val parsedSchedule: NetworkResponse.Schedule
                if (schoolId == "0"){
                    parsedSchedule = testData(programmeId)
                }
                else {
                    val endpoint = Endpoint.Schedule(scheduleId = programmeId, schoolId = schoolId)
                    val fetchedSchedule: ApiResponse<NetworkResponse.Schedule> = kronoxManager.getSchedule(endpoint)
                    parsedSchedule = parseFetchedSchedules(fetchedSchedule)
                }
                updateUIWithFetchedSchedule(parsedSchedule, existingSchedule = currentSchedules)
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    status = SchedulePreviewStatus.ERROR
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
            is ApiResponse.Loading -> {
                throw Exception()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUIWithFetchedSchedule(fetchedSchedule: NetworkResponse.Schedule, existingSchedule: List<Schedule>){
        viewModelScope.launch(Dispatchers.Main) {
            if (!fetchedSchedule.days.any { it.events.isNotEmpty() }){
                status = SchedulePreviewStatus.EMPTY
                buttonState = ButtonState.DISABLED
            }else if (existingSchedule.map { it.scheduleId }.contains(fetchedSchedule.id)){
                isSaved = true
                buttonState = ButtonState.SAVED
                schedule = fetchedSchedule
                courseColorsForPreview = realmManager.getCourseColors()
                status = SchedulePreviewStatus.LOADED
            } else{
                withContext(Dispatchers.Default){
                    val randomCourseColor = fetchedSchedule.assignCourseRandomColors()
                    withContext(Dispatchers.Main){
                        courseColorsForPreview = randomCourseColor
                        schedule = fetchedSchedule
                        buttonState = ButtonState.NOT_SAVED
                        status = SchedulePreviewStatus.LOADED
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
        schoolId: String
    ){
        buttonState = ButtonState.LOADING
        viewModelScope.launch {
            if (!isSaved) {
                schedule?.toRealmSchedule(scheduleRequiresAuth(schoolId), scheduleId, courseColorsForPreview)
                    ?.let { realmSchedule ->
                        realmManager.saveSchedule(realmSchedule)
                        isSaved = true
                        buttonState = ButtonState.SAVED
                    }
            } else {
                val realmSchedule = realmManager.getScheduleByScheduleId(scheduleId)
                realmSchedule?.let {
                    realmManager.deleteSchedule(it)
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

// fake data
@RequiresApi(Build.VERSION_CODES.O)
fun testData(programmeId: String): NetworkResponse.Schedule{
    val currentDate = Calendar.getInstance()
//    val targetFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val days: MutableList<NetworkResponse.Day> = mutableListOf()
    for (i in 0 .. 10){

        val event1 = eventGen(i, currentDate, hourStartTime = 9)
        val event2 = eventGen(i, currentDate, hourStartTime = 13)

        val events = listOf(event1, event2)


        val day = NetworkResponse.Day(
            name = "testName$i",
            date = isoDateFormatterDate.format(currentDate.time),
            isoString = isoDateFormatterDate.format(currentDate.time),
            weekNumber = currentDate.get(Calendar.WEEK_OF_YEAR),
            events = events,
        )
        days.add(day)
        currentDate.add(Calendar.WEEK_OF_MONTH, 1)
    }

    val schedule = NetworkResponse.Schedule(
        id = programmeId,
        cachedAt = "",
        days = days.toList())

    return schedule
}

@RequiresApi(Build.VERSION_CODES.O)
fun eventGen(offset: Int, calendar: Calendar, hourStartTime: Int): NetworkResponse.Event{

    calendar.set(Calendar.HOUR_OF_DAY, hourStartTime)
    calendar.set(Calendar.MINUTE, 0)

//    calendar.time

//    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val course = NetworkResponse.Course(
        id = "0",
        swedishName = "testSwedishName $offset, $hourStartTime",
        englishName = "testEnglishName $offset, $hourStartTime"
    )

    val location = NetworkResponse.Location(
        id = "testId",
        name = "testName $offset, $hourStartTime" ,
        building = "testBuilding $offset, $hourStartTime",
        floor = "testFloor $offset, $hourStartTime",
        maxSeats = 30
    )

    val teacher = NetworkResponse.Teacher(
        id = "testId $offset, $hourStartTime",
        firstName = "TestFirstName $offset, $hourStartTime",
        lastName = "TestLastName $offset, $hourStartTime"
    )


    val from = calendar.time
    calendar.add(Calendar.HOUR, 1)
    val to = calendar.time

    val event  = NetworkResponse.Event(
        id = "testId $offset, $hourStartTime",
        title = "TestTittle $offset, $hourStartTime",
        course = course,
        from = from.toIsoString(),
        to = to.toIsoString(),
        locations = listOf(location),
        teachers = listOf(teacher),
        isSpecial = false,
        lastModified = "never"
    )

    return event
}
