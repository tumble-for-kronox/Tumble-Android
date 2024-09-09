package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.extensions.models.timelotHasAvailable
import tumble.app.tumble.extensions.presentation.convertToHoursAndMinutesISOString

@Composable
fun TimeslotDropdown(
    resource: NetworkResponse.KronoxResourceElement,
    timeslots: List<NetworkResponse.TimeSlot>,
    selectedIndex: MutableState<Int>,
    onIndexChange: (Int) -> Unit
) {
    var isSelecting = remember { mutableStateOf(false) }
    var selectionTitle = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 8.dp)
            .padding(horizontal = 15.dp)
            .clickable {
                scope.launch {
                    isSelecting.value = !isSelecting.value
                }
            }
            .animateContentSize()
            .padding(bottom = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectionTitle.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
        }

        if (isSelecting.value) {
            Divider(color = Color.White)
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                timeslots.forEachIndexed { index, timeslot ->
                    timeslot.id?.let { timeslotId ->
                        if (resource.availabilities.timelotHasAvailable(timeslotId)) {
                            val start = timeslot.from?.convertToHoursAndMinutesISOString()
                            val end = timeslot.to?.convertToHoursAndMinutesISOString()

                            if (start != null && end != null) {
                                DropdownMenuItemView(
                                    isSelecting = isSelecting,
                                    selectionTitle = selectionTitle,
                                    selectedIndex = selectedIndex,
                                    item = DropdownItem(
                                        id = index,
                                        title = "$start - $end",
                                        onSelect = {
                                            onIndexChange(index)
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(timeslots, selectedIndex.value) {
        timeslots.getOrNull(selectedIndex.value)?.let { timeslot ->
            val start = timeslot.from?.convertToHoursAndMinutesISOString()
            val end = timeslot.to?.convertToHoursAndMinutesISOString()
            if (start != null && end != null) {
                selectionTitle.value = "$start - $end"
            }
        }
    }
}

@Composable
fun DropdownMenuItemView(
    isSelecting: MutableState<Boolean>,
    selectionTitle: MutableState<String>,
    selectedIndex: MutableState<Int>,
    item: DropdownItem
) {
    Button(
        onClick = {
            isSelecting.value = false
            selectionTitle.value = item.title
            item.onSelect()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface
            )
            if (selectedIndex.value == item.id) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

data class DropdownItem(
    val id: Int,
    val title: String,
    val onSelect: () -> Unit
)
