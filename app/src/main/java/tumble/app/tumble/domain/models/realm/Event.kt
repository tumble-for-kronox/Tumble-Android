package tumble.app.tumble.domain.models.realm

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import tumble.app.tumble.domain.models.util.DateComponents
import tumble.app.tumble.utils.isoDateFormatterNoTimeZone
import java.time.ZonedDateTime
import java.util.Calendar

open class Event(
) : RealmObject {
    var eventId: String = ""
    var title: String = ""
    var course: Course? = null
    var from: String = ""
    var to: String = ""
    var locations: RealmList<Location>? = null
    var teachers: RealmList<Teacher>? = null
    var isSpecial: Boolean = false
    var lastModified: String = ""
    var schoolId: String = ""

    val dateComponents: DateComponents?
        get() {

            return try {
                val dateTime = ZonedDateTime.parse(from)
                DateComponents(
                    year = dateTime.year,
                    month = dateTime.monthValue,
                    day = dateTime.dayOfMonth,
                    hour = dateTime.hour,
                    minute = dateTime.minute,
                    second = dateTime.second
                )
            } catch (e: Exception) {
                null
            }
        }

    fun toMap(): Map<String, Any> {
        val dictionary = mutableMapOf<String, Any>()

        dictionary["title"] = title
        dictionary["schoolId"] = schoolId
        dictionary["from"] = from
        dictionary["to"] = to
        dictionary["eventId"] = eventId
        dictionary["isSpecial"] = isSpecial
        dictionary["lastModified"] = lastModified

        // Handling course object
        val courseDict = mutableMapOf<String, String?>()
        course?.let {
            courseDict["englishName"] = it.englishName
            courseDict["swedishName"] = it.englishName
            courseDict["courseId"] = it.courseId
            courseDict["color"] = it.color
        }
        dictionary["course"] = courseDict

        locations?.let { locations -> {
            val locationsArray = locations.map { location ->
                mapOf(
                    "name" to location.name,
                    "locationId" to location.locationId,
                    "building" to location.building,
                    "floor" to location.floor,
                    "maxSeats" to location.maxSeats
                )
            }
            dictionary["locations"] = locationsArray
        } }

        teachers?.let { teachers -> {
            val teachersArray = teachers.map { teacher ->
                mapOf(
                    "firstName" to teacher.firstName,
                    "lastName" to teacher.lastName,
                    "teacherId" to teacher.teacherId
                )
            }
            dictionary["teachers"] = teachersArray
        } }

        return dictionary
    }
}

