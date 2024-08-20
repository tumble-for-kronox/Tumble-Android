package tumble.app.tumble.presentation.views.bookmarks.List

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
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
        contentPadding = PaddingValues(0.dp)
    ) {
        VerboseEventButtonLabel(event = event)
    }
}