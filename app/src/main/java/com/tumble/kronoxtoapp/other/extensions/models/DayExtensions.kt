package com.tumble.kronoxtoapp.other.extensions.models

import com.tumble.kronoxtoapp.domain.models.realm.Day
import com.tumble.kronoxtoapp.utils.isoDateFormatter
import com.tumble.kronoxtoapp.utils.preprocessDateString
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


fun List<Day>.filterValidDays(): List<Day> {
    return this.filter{ it.isValidDay() }
}


fun List<Day>.groupByWeek(): Map<Int, List<Day>>{
    val weeks: MutableMap<Int, MutableList<Day>> = mutableMapOf()

    for (day in this){
        weeks[day.weekNumber] = weeks.getOrDefault(day.weekNumber, mutableListOf()).apply { add(day) }
    }
    return weeks
}


fun Day.isValidDay(): Boolean {
    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString?.substring(0,10)?.let { preprocessDateString(it) })
    return startOfToday <= dayDate
}