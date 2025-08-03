package tumble.app.tumble.presentation.screens.settings.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
    onDelete: (String) -> Unit,
    onToggle: (Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isToggled by remember { mutableStateOf(schedule.toggled) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = schedule.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                maxLines = Int.MAX_VALUE
            )
            Text(
                text = schedule.scheduleId,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                fontSize = 14.sp,
                maxLines = Int.MAX_VALUE
            )
        }

        Switch(
            checked = isToggled,
            onCheckedChange = { checked ->
                isToggled = checked
                onToggle(checked)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f),
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f)
            )
        )
    }
}


