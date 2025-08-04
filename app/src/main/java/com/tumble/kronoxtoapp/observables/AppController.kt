package com.tumble.kronoxtoapp.observables

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.tumble.kronoxtoapp.domain.models.presentation.EventDetailsSheetModel
import com.tumble.kronoxtoapp.domain.models.presentation.ResourceSelectionModel
import com.tumble.kronoxtoapp.domain.models.presentation.SearchPreviewModel
import javax.inject.Inject

/**
 * Observable state from views to open sheets from
 * pressing external notifications in order to navigate
 * to certain views with attributes. These values are global
 * and meant to be controllable from anywhere in the app.
 *
 * Might not be best practice.. but hey, it works.
 */
class AppController @Inject constructor() : ViewModel() {
    companion object {
        val shared: AppController by lazy { AppController() }
    }

    var searchPreview by mutableStateOf<SearchPreviewModel?>(null)
    var eventSheet by mutableStateOf<EventDetailsSheetModel?>(null)
    var resourceModel by mutableStateOf<ResourceSelectionModel?>(null)
    private var _isUpdatingBookmarks = MutableStateFlow(false)
    val isUpdatingBookmarks = _isUpdatingBookmarks.asStateFlow()

    fun setIsUpatingBookmarks(value: Boolean) {
        Log.d("AppController", "Setting 'isUpdatingBookmarks' to $value")
        this._isUpdatingBookmarks.value = value
    }
}