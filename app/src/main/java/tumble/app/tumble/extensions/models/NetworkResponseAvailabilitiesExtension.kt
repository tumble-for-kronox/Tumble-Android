package tumble.app.tumble.extensions.models

import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.network.availabilities

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

fun availabilities.getAvailabilityValues(timelotId: Int): List<NetworkResponse.AvailabilityValue>{

    val availabilities = this?: return emptyList()
    val availabilityValueResult = mutableListOf<NetworkResponse.AvailabilityValue>()
    for ((location, availabilityValues) in availabilities){
        val availabilityValue = availabilityValues[timelotId]
        if(availabilityValue?.availability == NetworkResponse.AvailabilityEnum.AVAILABLE){
            availabilityValueResult.add(availabilityValue.copy(locationID = location))
        }
    }
    return availabilityValueResult
}