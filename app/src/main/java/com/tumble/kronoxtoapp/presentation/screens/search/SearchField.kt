package com.tumble.kronoxtoapp.presentation.screens.search

import android.app.Activity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.extensions.presentation.View.hideKeyboard

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
    val searchBarText by remember {
        mutableStateOf(searchBarText)
    }
    val searching by remember {
        mutableStateOf(searching)
    }
    val enabled = selectedSchool.value != null
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val blur = animateDpAsState(
        targetValue = if (enabled) 0.dp else 2.5.dp,
    )

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
    DockedSearchBar(query = searchBarText.value,
        onQueryChange = { searchBarText.value = it},
        onSearch = { searchAction() },
        active = false,
        enabled = enabled,
        placeholder = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface.copy(.25f),
                fontWeight = FontWeight.Normal
            )},
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
            .blur(blur.value)
            .fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary.copy(.075f),
            dividerColor = MaterialTheme.colorScheme.primary,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
        ),
    ) {}
}

@Composable
fun InBarButtons(searchAction: () -> Unit, searchFieldAction: () -> Unit){
    Row{  IconButton(onClick = { searchAction() },) {
        Icon(imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp))
    }
        IconButton(onClick = { searchFieldAction() },) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp))
        }
    }
}