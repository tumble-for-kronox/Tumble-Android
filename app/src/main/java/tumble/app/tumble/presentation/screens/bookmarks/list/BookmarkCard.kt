package tumble.app.tumble.presentation.screens.bookmarks.list

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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