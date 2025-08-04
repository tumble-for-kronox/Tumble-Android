package com.tumble.kronoxtoapp.extensions.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.utils.isoDateFormatter
import com.tumble.kronoxtoapp.utils.preprocessDateString
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkResponse.Day.isValidDay(): Boolean{

//    LocalDate.parse(this.isoString)
//    this.isoString.toLocalDateTime()

    val startOfToday = LocalDate.now()
    val dayDate = LocalDate.parse(this.isoString.substring(0,10))
    return startOfToday <= dayDate
}

fun List<NetworkResponse.Day>.ordered(): List<NetworkResponse.Day> {
    return this.filterNotNull().sortedBy {
        val processedDateString = preprocessDateString(it.isoString)
        isoDateFormatter.parse(processedDateString)
    }
}