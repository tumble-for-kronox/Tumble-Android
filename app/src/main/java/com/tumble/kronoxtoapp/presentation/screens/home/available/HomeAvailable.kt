package com.tumble.kronoxtoapp.presentation.screens.home.available

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.domain.models.realm.Event

@Composable
fun HomeAvailable(
    nextClass: Event?,
    onEventSelection: (Event) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        NextClass(nextClass = nextClass, onEventSelection)
        Spacer(modifier = Modifier.weight(1f))
    }
}