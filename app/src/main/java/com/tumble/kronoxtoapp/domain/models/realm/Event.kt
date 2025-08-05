package com.tumble.kronoxtoapp.domain.models.realm

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
            val date = isoDateFormatterNoTimeZone.parse(from)
            return if (date != null) {
                Calendar.getInstance().apply {
                    time = date
                }
            } else null
        }
}

