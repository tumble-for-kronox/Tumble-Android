package tumble.app.tumble.extensions.models

import io.realm.kotlin.ext.toRealmList
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.realm.Course
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.domain.models.realm.Location
import tumble.app.tumble.domain.models.realm.Teacher

fun NetworkResponse.Event.toRealmEvent(courseColorsForPreview: Map<String, String>): Event {
    val course = Course()
    course.courseId = this.course.id
    course.swedishName = this.course.swedishName
    course.englishName = this.course.englishName
    course.color =  courseColorsForPreview.getOrDefault(this.course.id, "#FFFFFF")
    val locations: MutableList<Location> = mutableListOf()
    for (responseLocation in this.locations){
        val location = Location()
        location.locationId = responseLocation.id
        location.name = responseLocation.name
        location.building = responseLocation.building
        location.floor = responseLocation.floor
        location.maxSeats = responseLocation.maxSeats
        locations.add(location)
    }

    val teachers: MutableList<Teacher> = mutableListOf()
    for (responseTeacher in this.teachers){
        val teacher = Teacher()
        teacher.teacherId = responseTeacher.id
        teacher.firstName = responseTeacher.firstName
        teacher.lastName = responseTeacher.lastName
        teachers.add(teacher)

    }
    val event = Event()
    event.eventId = this.id
    event.title = this.title
    event.course = course
    event.from = this.from
    event.to = this.to
    event.locations = locations.toRealmList()
    event.teachers = teachers.toRealmList()
    event.isSpecial = this.isSpecial
    event.lastModified = this.lastModified
    return event
}