package com.tumble.kronoxtoapp.other.extensions.models

import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.utils.isoDateFormatter
import com.tumble.kronoxtoapp.utils.preprocessDateString
import java.time.LocalDate


fun NetworkResponse.Day.isValidDay(): Boolean{
    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString.substring(0,10))
    return startOfToday <= dayDate
}

fun List<NetworkResponse.Day>.ordered(): List<NetworkResponse.Day> {
    return this.sortedBy {
        val processedDateString = preprocessDateString(it.isoString)
        isoDateFormatter.parse(processedDateString)
    }
}