package tumble.app.tumble.presentation.views.search

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.extensions.presentation.View.hideKeyboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    search: (() -> Unit)?,
    clearSearch: (() -> Unit)?,
    title: String,
    searchBarText: MutableState<String>,
    searching: MutableState<Boolean>,
    selectedSchool: MutableState<School?>
){
    var searchBarText by remember {
        mutableStateOf(searchBarText)
    }
    var searching by remember {
        mutableStateOf(searching)
    }
    var enabled = selectedSchool.value != null
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    fun searchAction(){
        search?.invoke()
        val activity = context as? Activity
        activity?.hideKeyboard()
    }
    fun searchFieldAction(){
        clearSearch?.invoke()
        searchBarText.value = ""
        val activity = context as? Activity
        focusManager.clearFocus()
        activity?.hideKeyboard()
        searching.value = false
    }

    Row (
        modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DockedSearchBar(query = searchBarText.value,
            onQueryChange = { searchBarText.value = it},
            onSearch = { searchAction() },
            active = false,
            enabled = enabled,
            placeholder = {Text(text = title, color = Color.Black)},
            onActiveChange = {},
            trailingIcon = { if (searching.value) {
                InBarButtons(
                        searchAction = { searchAction() },
                        searchFieldAction = { searchFieldAction() }
                    )
                }
            },
            modifier = Modifier
                .onFocusChanged { searching.value = if (it.isFocused) true else enabled}
                .blur(if (!enabled) 2.5.dp else 0.dp)
        ) {}
    }
}

@Composable
fun InBarButtons(searchAction: () -> Unit, searchFieldAction: () -> Unit){
    Row{  IconButton(onClick = { searchAction() },) {
        Icon(imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(24.dp))
    }
        IconButton(onClick = { searchFieldAction() },) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp))
        }
    }
}