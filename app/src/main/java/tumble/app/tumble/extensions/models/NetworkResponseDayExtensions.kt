package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import tumble.app.tumble.utils.isoDateFormatter
import tumble.app.tumble.utils.isoDateFormatterDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Day.isValidDay(): Boolean{

//    LocalDate.parse(this.isoString)
//    this.isoString.toLocalDateTime()

    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString, DateTimeFormatter.ISO_DATE)
    return startOfToday <= dayDate
}

fun List<NetworkResponse.Day>.ordered(): List<NetworkResponse.Day> {
    return this.filterNotNull().sortedBy {  isoDateFormatterDate.parse(it.isoString) }
}