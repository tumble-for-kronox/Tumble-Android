package com.tumble.kronoxtoapp.domain.models.realm

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Schedule(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var scheduleId: String = ""
    var cachedAt: String = ""
    var days: RealmList<Day>? = null
    var toggled: Boolean = true
    var schoolId: String = ""
    var requiresAuth: Boolean = false
    var title: String = ""
}
