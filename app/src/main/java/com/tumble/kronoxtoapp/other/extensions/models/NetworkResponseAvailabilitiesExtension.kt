package com.tumble.kronoxtoapp.other.extensions.models

import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.network.availabilities

fun availabilities.timeslotHasAvailable(timeslotId: Int):Boolean {

    val availabilities = this ?: return false
    for ((_, availabilityValues) in availabilities){
        val availability = availabilityValues[timeslotId]?.availability

        if (availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
            return true
        }
    }
    return false
}

fun availabilities.getFirstTimeSlotWithAvailability(numOfTimeSlots: Int): Int {
    for(i in 0..numOfTimeSlots){
        for(j in this!!.values){
            if(j[i]!!.availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
                return i
            }
        }
    }
    return 1
}

fun availabilities.getAvailabilityValues(timeslotId: Int): List<NetworkResponse.AvailabilityValue>{

    val availabilities = this?: return emptyList()
    val availabilityValueResult = mutableListOf<NetworkResponse.AvailabilityValue>()
    for (availabilityValues in availabilities.values){
        val availabilityValue = availabilityValues[timeslotId]
        if(availabilityValue?.availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
            availabilityValueResult.add(availabilityValue)
        }
    }
    return availabilityValueResult
}