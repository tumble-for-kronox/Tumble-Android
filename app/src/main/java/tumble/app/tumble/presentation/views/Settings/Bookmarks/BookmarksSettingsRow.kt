package tumble.app.tumble.presentation.views.Settings.Bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Schedule

@Composable
fun BookmarkSettingsRow(
    schedule: Schedule,
    index: Int,
    onDelete: (Int, String) -> Unit
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        var isToggled by remember { mutableStateOf(schedule.toggled) }

        Column {
            Text(
                text = "Schedule Name", // To be Schedule.title
                color = MaterialTheme.colors.onSurface,
                fontSize = 18.sp
            )
            Text(
                text = schedule.scheduleId,
                color = MaterialTheme.colors.onSurface.copy(alpha = .4f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isToggled,
            onCheckedChange = { checked ->
                isToggled = checked
                schedule.toggled = checked
                // Trigger the widget update when the toggle changes
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = .5f),
                uncheckedThumbColor = MaterialTheme.colors.onSurface.copy(alpha = .4f),
                uncheckedTrackColor = MaterialTheme.colors.onSurface.copy(alpha = .2f),
                uncheckedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = .4f)
            )
        )
    }
}

