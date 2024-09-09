package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import tumble.app.tumble.utils.isoDateFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Day.isValidDay(): Boolean{

//    LocalDate.parse(this.isoString)
//    this.isoString.toLocalDateTime()

    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString.substring(0,10))
    return startOfToday <= dayDate
}

fun List<NetworkResponse.Day>.ordered(): List<NetworkResponse.Day> {
    return this.filterNotNull().sortedBy {  isoDateFormatter.parse(it.isoString) }
}