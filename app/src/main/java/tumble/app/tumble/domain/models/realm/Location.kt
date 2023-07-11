package tumble.app.tumble.domain.models.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class Location(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId? = null
    var locationId: String? = null
    var name: String? = null
    var building: String? = null
    var floor: String? = null
    var maxSeats: Int = 0
}
