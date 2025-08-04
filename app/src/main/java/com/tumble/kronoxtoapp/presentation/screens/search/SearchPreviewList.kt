package com.tumble.kronoxtoapp.presentation.screens.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.extensions.models.flatten
import com.tumble.kronoxtoapp.extensions.models.ordered
import com.tumble.kronoxtoapp.extensions.models.toRealmEvent
import com.tumble.kronoxtoapp.presentation.components.buttons.VerboseEventButtonLabel
import com.tumble.kronoxtoapp.presentation.viewmodels.SearchPreviewViewModel


@Composable
fun SearchPreviewList(
    viewModel: SearchPreviewViewModel
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 2.5.dp).fillMaxHeight()
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        viewModel.schedule?.flatten()?.ordered()?.forEach{day ->
            if (day.events.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        DayResponseHeader(day = day)
                        day.events.forEach { event ->
                            VerboseEventButtonLabel(event = event.toRealmEvent(viewModel.courseColorsForPreview))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}