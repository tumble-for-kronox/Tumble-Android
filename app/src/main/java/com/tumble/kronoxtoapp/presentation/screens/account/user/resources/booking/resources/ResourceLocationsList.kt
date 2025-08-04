package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.DetailItem
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceViewModel
import com.tumble.kronoxtoapp.utils.isoVerboseDateFormatter
import java.util.Date

@Composable
fun ResourceLocationsList(
    parentViewModel: ResourceViewModel = hiltViewModel(),
    selectedPickerDate: Date,
    navigateToResourceSelection: (NetworkResponse.KronoxResourceElement, Date) -> Unit
) {
    val allResources = parentViewModel.allResources.collectAsState()

    val availableResources = allResources.value?.filter { resource ->
        val availableCounts = resource.availabilities?.let { calcAvailability(it) } ?: 0
        availableCounts > 0
    } ?: emptyList()

    if (availableResources.isEmpty()) {
        Info("No available resources")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(availableResources.size) { resourceIndex ->
                val resource = availableResources[resourceIndex]
                val availableCounts = resource.availabilities?.let { calcAvailability(it) } ?: 0

                ResourceLocationItem(
                    resource = resource,
                    selectedPickerDate = selectedPickerDate,
                    availableCounts = availableCounts,
                    onClick = {
                        navigateToResourceSelection(resource, selectedPickerDate)
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(60.dp)) }
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = resource.name ?: stringResource(R.string.no_name),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                DetailItem(Icons.Default.Event, isoVerboseDateFormatter.format(selectedPickerDate))
                DetailItem(Icons.Default.AccessTime, "${stringResource(R.string.available_timeslots)}: $availableCounts")
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}

fun calcAvailability(availabilities: Map<String, Map<Int, NetworkResponse. AvailabilityValue>>): Int{
    return availabilities.map {location -> location.value.filter { it.value.availability == NetworkResponse.AvailabilityEnum.AVAILABLE }.size}.sum()
}