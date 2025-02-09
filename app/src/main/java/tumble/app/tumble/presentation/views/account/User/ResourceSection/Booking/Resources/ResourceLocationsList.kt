package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
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
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.viewmodels.ResourceViewModel
import tumble.app.tumble.presentation.views.account.User.ResourceSection.InformationView
import tumble.app.tumble.utils.isoVerboseDateFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResourceLocationsList(
    parentViewModel: ResourceViewModel = hiltViewModel(),
    selectedPickerDate: Date,
    navigateToResourceSelection: (NetworkResponse.KronoxResourceElement, Date) -> Unit
) {
    val allResources = parentViewModel.allResources.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
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
        item { Spacer(modifier = Modifier.height(60.dp)) }
    }
}

@Composable
fun ResourceLocationItem(
    resource: NetworkResponse.KronoxResourceElement,
    selectedPickerDate: Date,
    availableCounts: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = resource.name ?: stringResource(R.string.no_name),
                fontSize = 17.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            InformationView(Icons.Default.Event, isoVerboseDateFormatter.format(selectedPickerDate))
            InformationView(Icons.Default.AccessTime, "${stringResource(R.string.available_timeslots)}: $availableCounts")
        }
    }
}

fun calcAvailability(availabilities: Map<String, Map<Int, NetworkResponse. AvailabilityValue>>): Int{
    return availabilities.map {location -> location.value.filter { it.value.availability == NetworkResponse.AvailabilityEnum.AVAILABLE }.size}.sum()
}