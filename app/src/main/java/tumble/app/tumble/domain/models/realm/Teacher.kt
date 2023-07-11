package tumble.app.tumble.domain.models.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Teacher(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = null
    var teacherId: String? = null
    var firstName: String? = null
    var lastName: String? = null
}
