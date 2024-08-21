package tumble.app.tumble.presentation.views.bookmarks.List

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.components.buttons.VerboseEventButtonLabel

@Composable
fun BookmarkCard(
     onTapCard: (Event) -> Unit,
     event: Event,
     isLast: Boolean
){
    Button(
        onClick = { onTapCard(event) },
        contentPadding = PaddingValues(vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
    ) {
        VerboseEventButtonLabel(event = event)
    }
}