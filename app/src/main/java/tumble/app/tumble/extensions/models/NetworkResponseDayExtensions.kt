package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.toLocalDateTime
import tumble.app.tumble.utils.isoDateFormatter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Day.isValidDay(): Boolean{
    val startOfToday = LocalDate.now().atStartOfDay()
    val dayDate = this.isoString.toLocalDateTime()
    return startOfToday <= dayDate
}

fun List<NetworkResponse.Day>.ordered(): List<NetworkResponse.Day> {
    return this.filterNotNull().sortedBy { isoDateFormatter.parse(it.isoString) }
}