package tumble.app.tumble.extensions.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import tumble.app.tumble.domain.models.realm.Day
import tumble.app.tumble.utils.isoDateFormatter
import tumble.app.tumble.utils.isoDateFormatterDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun List<Day>.ordered(): List<Day> {
    return this.sortedBy { it.isoString?.let { it1 -> isoDateFormatterDate.parse(it1) } }
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
    val dayDate = LocalDate.parse(this.isoString, DateTimeFormatter.ISO_DATE)
    return startOfToday <= dayDate
}