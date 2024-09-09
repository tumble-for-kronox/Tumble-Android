package tumble.app.tumble.presentation.views.Settings.List

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme

@Composable
fun SettingsListGroup(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(10.dp)) // Replace Color.LightGray with your surface color
            .padding(10.dp) // Inner padding
    ) {
        content()
    }
}