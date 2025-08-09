package com.tumble.kronoxtoapp.domain.models.realm

import com.tumble.kronoxtoapp.utils.DateUtils
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import com.tumble.kronoxtoapp.utils.isoDateFormatterNoTimeZone
import java.util.Calendar

open class Event(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var eventId: String = ""
    var title: String = ""
    var course: Course? = null
    var from: String = ""
    var to: String = ""
    var locations: RealmList<Location>? = null
    var teachers: RealmList<Teacher>? = null
    var isSpecial: Boolean = false
    var lastModified: String = ""

    val dateComponents: Calendar?
        get() {
            return try {
                val localDateTime = DateUtils.toUserLocalDateTime(from)
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, localDateTime.year)
                    set(Calendar.MONTH, localDateTime.monthValue - 1)
                    set(Calendar.DAY_OF_MONTH, localDateTime.dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, localDateTime.hour)
                    set(Calendar.MINUTE, localDateTime.minute)
                    set(Calendar.SECOND, localDateTime.second)
                }
            } catch (e: Exception) {
                val date = isoDateFormatterNoTimeZone.parse(from)
                if (date != null) {
                    Calendar.getInstance().apply {
                        time = date
                    }
                } else null
            }
        }
}

