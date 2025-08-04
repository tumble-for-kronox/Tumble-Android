package com.tumble.kronoxtoapp.domain.models.realm

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Day(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = ObjectId()
    var name: String? = null
    var date: String? = null
    var isoString: String? = null
    var weekNumber: Int = 0
    var events: RealmList<Event>? = null
}
