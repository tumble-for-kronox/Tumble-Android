package com.tumble.kronoxtoapp.presentation.screens.bookmarks.list

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tumble.kronoxtoapp.domain.models.realm.Event
import com.tumble.kronoxtoapp.other.extensions.presentation.noRippleClickable
import com.tumble.kronoxtoapp.presentation.components.buttons.VerboseEventButtonLabel

@Composable
fun BookmarkCard(
    onTapCard: (Event) -> Unit,
    event: Event,
    isLast: Boolean
) {
    Box(Modifier.noRippleClickable { onTapCard(event) }) {
        VerboseEventButtonLabel(event = event)
    }
}