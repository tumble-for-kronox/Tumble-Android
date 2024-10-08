package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import java.time.LocalDate
import java.util.Date

@Composable
fun ResourceLocationsList(
    parentViewModel: ResourceViewModel = hiltViewModel(),
    selectedPickerDate: Date,
    navigateToResourceSelection: (NetworkResponse.KronoxResourceElement, Date) -> Unit
) {

    val allResources = parentViewModel.allResources.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {

        items((allResources.value?: emptyList()).size) { resourceIndex ->

            allResources.value?.get(resourceIndex)?.let {

                val availableCounts = it.availabilities?.let { it1 -> calcAvailability(it1) }?: 0

                ResourceLocationItem(
                    resource = it,
                    selectedPickerDate = selectedPickerDate,
                    availableCounts = availableCounts,
                    onClick = {
                        if (availableCounts > 0) {
                            navigateToResourceSelection(it, selectedPickerDate)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ResourceLocationItem(
    resource: NetworkResponse.KronoxResourceElement,
    selectedPickerDate: Date,
    availableCounts: Int,
    onClick: () -> Unit
) {
    val backgroundColor = if (availableCounts > 0) {
        Color.White
    } else {
        Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = resource.name ?: "No name",
                fontSize = 18.sp,
                color = Color.Black, // Replace with your theme color
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.7f)
                )
                Text(
                    text =  selectedPickerDate.toString(),
                    fontSize = 15.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.7f)
                )
                Text(
                    text = "Available timeslots: $availableCounts",
                    fontSize = 15.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
        }
    }
}

fun calcAvailability(availabilities: Map<String, Map<Int, NetworkResponse. AvailabilityValue>>): Int{
    return availabilities.map {location -> location.value.filter { it.value.availability == NetworkResponse.AvailabilityEnum.AVAILABLE }.size}.sum()
}