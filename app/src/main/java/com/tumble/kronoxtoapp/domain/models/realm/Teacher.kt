package com.tumble.kronoxtoapp.domain.models.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Teacher(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = ObjectId()
    var teacherId: String? = null
    var firstName: String? = null
    var lastName: String? = null
}
