package tumble.app.tumble.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.utils.isoDateFormatter
import tumble.app.tumble.utils.preprocessDateString
import java.time.LocalDate

fun List<Day>.ordered(): List<Day> {

    return this.sortedBy {
        it.isoString?.let { isoString ->
            val processedDateString = preprocessDateString(isoString)
            isoDateFormatter.parse(processedDateString)
        }
    }
}

fun List<Day>.filterEmptyDays(): List<Day> {
    return this.filter{ it.events?.isNotEmpty() ?: false }
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<Day>.filterValidDays(): List<Day> {
    return this.filter{ it.isValidDay() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<Day>.groupByWeek(): Map<Int, List<Day>>{
    val weeks: MutableMap<Int, MutableList<Day>> = mutableMapOf()

    for (day in this){
        weeks[day.weekNumber] = weeks.getOrDefault(day.weekNumber, mutableListOf()).apply { add(day) }
    }
    return weeks
}

@RequiresApi(Build.VERSION_CODES.O)
fun Day.isValidDay(): Boolean {
    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString?.substring(0,10)?.let { preprocessDateString(it) })
    return startOfToday <= dayDate
}