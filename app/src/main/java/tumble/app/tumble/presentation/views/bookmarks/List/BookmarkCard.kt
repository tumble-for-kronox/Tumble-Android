package tumble.app.tumble.presentation.views.bookmarks.List

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.components.buttons.VerboseEventButtonLabel

@Composable
fun BookmarkCard(
     onTapCard: (Event) -> Unit,
     event: Event,
     isLast: Boolean
){
    Box(Modifier.noRippleClickable { onTapCard(event) }) {
        VerboseEventButtonLabel(event = event)
    }
}