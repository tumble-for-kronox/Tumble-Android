package tumble.app.tumble.presentation.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tumble.app.tumble.domain.enums.ButtonState
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator

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
                CustomProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            }
            ButtonState.SAVED -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkRemove,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            ButtonState.NOT_SAVED -> {
                Icon(
                    imageVector = Icons.Filled.BookmarkAdd,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

