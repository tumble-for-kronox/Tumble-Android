package com.tumble.kronoxtoapp.extensions.models

import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.availabilities

fun availabilities.timelotHasAvailable(timelotId: Int):Boolean {

    val availabilities = this ?: return false
    for ((_, availabilityValues) in availabilities){
        val availability = availabilityValues[timelotId]?.availability

        if (availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
            return true
        }
    }
    return false
}

fun availabilities.getFirstTimeSlotWithAvailability(numOfTimeSlots: Int): Int{
    for(i in 0..numOfTimeSlots){
        for(j in this!!.values){
            if(j[i]!!.availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
                return i
            }
        }
    }
    return 1
}

fun availabilities.getAvailabilityValues(timelotId: Int): List<NetworkResponse.AvailabilityValue>{

    val availabilities = this?: return emptyList()
    val availabilityValueResult = mutableListOf<NetworkResponse.AvailabilityValue>()
    for (availabilityValues in availabilities.values){
        val availabilityValue = availabilityValues[timelotId]
        if(availabilityValue?.availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
            availabilityValueResult.add(availabilityValue)
        }
    }
    return availabilityValueResult
}