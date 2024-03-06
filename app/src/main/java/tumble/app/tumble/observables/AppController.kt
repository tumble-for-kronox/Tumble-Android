package tumble.app.tumble.observables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tumble.app.tumble.domain.models.presentation.EventDetailsSheetModel
import tumble.app.tumble.presentation.views.navigation.BottomNavItem
import javax.inject.Inject

/**
 * Observable state from views to open sheets from
 * pressing external notifications in order to navigate
 * to certain views with attributes. These values are global
 * and meant to be controllable from anywhere in the app.
 */
class AppController @Inject constructor() : ViewModel() {
    companion object {
        val shared: AppController by lazy { AppController() }
    }

    var eventSheet by mutableStateOf<EventDetailsSheetModel?>(null)
    var selectedAppTab by mutableStateOf(BottomNavItem.HOME)
    private val _isUpdatingBookmarks = MutableStateFlow(false)
    var isUpdatingBookmarks = _isUpdatingBookmarks.asStateFlow()
}