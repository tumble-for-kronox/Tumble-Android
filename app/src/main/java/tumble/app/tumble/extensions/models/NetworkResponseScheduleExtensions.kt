package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import io.realm.kotlin.ext.toRealmList
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.realm.Course
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.domain.models.realm.Location
import tumble.app.tumble.domain.models.realm.Schedule
import tumble.app.tumble.domain.models.realm.Teacher
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import tumble.app.tumble.utils.isoDateFormatter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Schedule.assignCourseRandomColors(): MutableMap<String, String> {
    val colors = mutableListOf("#f44336","#e91e63","#9c27b0","#673ab7",
        "#3f51b5","#2196f3","#03a9f4","#00bcd4",
        "#009688","#4caf50","#8bc34a","#cddc39",
        "#ffeb3b","#ffc107","#ff9800","#ff5722",
        "#795548","#9e9e9e","#607d8b","#333333")
    colors.shuffle()
    val courseColors: MutableMap<String, String> = mutableMapOf()

    for (day in days){
        for (event in day.events){
            val courseId = event.course.id
            if(!courseColors.containsKey(courseId)){
                val color = colors.firstOrNull()
                if (color != null){
                    courseColors[courseId] = color
                    colors.remove(color)
                }
            }
        }
    }
    return courseColors
}

fun NetworkResponse.Schedule.toRealmSchedule(scheduleRequiresAuth: Boolean, schoolId:String, existingCourseColors: Map<String, String> = emptyMap()): Schedule {
    val realmDays: MutableList<Day> = mutableListOf()
    val visitedColors = existingCourseColors.toMutableMap()
    for(responseDay in days){
        val realmEvents: MutableList<Event> = mutableListOf()

        for (responseEvent in responseDay.events){
            val courseId = responseEvent.course.id
            if(!visitedColors.containsKey(courseId)){
                visitedColors[courseId] = "#000000"
            }
            val course = Course()
            course.courseId = responseEvent.course.id
            course.swedishName = responseEvent.course.swedishName
            course.englishName = responseEvent.course.englishName
            course.color = visitedColors[courseId]?: "#FFFFFF"
            val locations: MutableList<Location> = mutableListOf()

            for (responseLocation in responseEvent.locations){
                val location = Location()
                location.locationId = responseLocation.id
                location.name = responseLocation.name
                location.building = responseLocation.building
                location.floor = responseLocation.floor
                location.maxSeats = responseLocation.maxSeats
                locations.add(location)
            }

            val teachers: MutableList<Teacher> = mutableListOf()
            for (responseTeacher in responseEvent.teachers){
                val teacher = Teacher()
                teacher.teacherId = responseTeacher.id
                teacher.firstName = responseTeacher.firstName
                teacher.lastName = responseTeacher.lastName
                teachers.add(teacher)
            }

            val event = Event()
            event.eventId = responseEvent.id
            event.title = HtmlCompat.fromHtml(responseEvent.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            event.course = course
            event.from = responseEvent.from
            event.to = responseEvent.to
            event.locations = locations.toRealmList()
            event.teachers = teachers.toRealmList()
            event.isSpecial = responseEvent.isSpecial
            event.lastModified = responseEvent.lastModified

            realmEvents.add(event)
        }

        val realmDay = Day()
        realmDay.name = responseDay.name
        realmDay.date = responseDay.date
        realmDay.isoString = responseDay.isoString
        realmDay.weekNumber = responseDay.weekNumber
        realmDay.events = realmEvents.toRealmList()

        realmDays.add(realmDay)
    }

    val schedule = Schedule()
    schedule.scheduleId = id
    schedule.cachedAt = cachedAt
    schedule.days = realmDays.toRealmList()
    schedule.scheduleId = schoolId
    schedule.requiresAuth = scheduleRequiresAuth

    return schedule
}

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Schedule.flatten(): List<NetworkResponse.Day> {
    return days.filter { it.isValidDay() }
}