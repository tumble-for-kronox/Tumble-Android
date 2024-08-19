package tumble.app.tumble.domain.models.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Course(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = ObjectId()
    var courseId: String? = null
    var swedishName: String? = null
    var englishName: String? = null
    var color: String? = null
}