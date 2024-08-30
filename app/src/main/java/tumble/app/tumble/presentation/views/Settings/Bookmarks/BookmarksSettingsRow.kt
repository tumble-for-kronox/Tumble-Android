package tumble.app.tumble.presentation.views.Settings.Bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.launch
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
            .padding(10.dp)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
            .padding(15.dp)
    ) {
        var isToggled by remember { mutableStateOf(schedule.toggled) }

        Text(
            text = schedule.scheduleId,
            color = MaterialTheme.colors.onSurface,
            fontSize = 18.sp
        )

        Switch(
            checked = isToggled,
            onCheckedChange = { checked ->
                isToggled = checked
                schedule.toggled = checked
                // Trigger the widget update when the toggle changes
            },
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary),
            modifier = Modifier.scale(2f)
        )



    }
}

