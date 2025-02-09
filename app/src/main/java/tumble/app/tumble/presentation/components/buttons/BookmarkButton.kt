package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.enums.ButtonState
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun BookmarkButton(
    bookmark: () -> Unit,
    buttonState: ButtonState
){
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = { coroutineScope.launch { bookmark() } } ) {
        when (buttonState){
            ButtonState.LOADING, ButtonState.DISABLED -> {
                CustomProgressIndicator(color = MaterialTheme.colors.onPrimary)
            }
            ButtonState.SAVED -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkRemove,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            ButtonState.NOT_SAVED -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkAdd,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

