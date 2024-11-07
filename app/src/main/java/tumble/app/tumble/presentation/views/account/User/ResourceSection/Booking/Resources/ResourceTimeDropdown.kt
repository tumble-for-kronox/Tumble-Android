package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking.Resources

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val isSelecting = remember { mutableStateOf(false) }
    val selectionTitle = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
            .padding(10.dp)
            .clickable {
                    isSelecting.value = !isSelecting.value
            }
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectionTitle.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface
            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.rotate(90f)
            )
        }
        if (isSelecting.value) {
            Divider(color = Color.White)
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                isSelecting.value = false
                selectionTitle.value = item.title
                item.onSelect()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
            modifier = Modifier.fillMaxWidth(),
            elevation = null,
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(Modifier.weight(1f))
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
}

data class DropdownItem(
    val id: Int,
    val title: String,
    val onSelect: () -> Unit
)
