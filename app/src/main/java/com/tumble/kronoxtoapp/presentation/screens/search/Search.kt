package com.tumble.kronoxtoapp.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.navigation.UriBuilder
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.viewmodels.SearchViewModel
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.SearchState

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Search(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.search)
    val state by viewModel.state.collectAsState()

    fun searchBoxNotEmpty(): Boolean{
        return viewModel.searchBarText.value.trim().isNotEmpty()
    }

     fun search(){
         viewModel.selectedSchool.value?.let { selectedSchool ->
             if (searchBoxNotEmpty()){
                 viewModel.universityImage = selectedSchool.logo
                 viewModel.search(
                     viewModel.searchBarText,
                     selectedSchoolId = selectedSchool.id
                 )
             }
         }
    }

    val openProgramme: (String, String) -> Unit = { programmeId, scheduleTitle ->
        viewModel.selectedSchool.value?.id?.let { selectedSchoolId ->
            navController.navigate(UriBuilder.buildSearchDetailsUri(programmeId,
                selectedSchoolId.toString(), scheduleTitle).toUri())
        }
    }

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(title = pageTitle)
        )
    }

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {

        when (state) {
            is SearchState.Initial -> {
                SearchInfo(schools = viewModel.schools, selectedSchool =  viewModel.selectedSchool)
            }
            is SearchState.Loading -> { CustomProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), color = MaterialTheme.colorScheme.primary) }
            is SearchState.Error -> { Box(modifier = Modifier.weight(1f)) {
                Info(title = stringResource(id = R.string.error_something_wrong), image = null)
            } }
            is SearchState.Empty -> { Box(modifier = Modifier.weight(1f)) {
                Info(title = stringResource(id = R.string.error_empty_schedule), image = null)
            } }
            is SearchState.Loaded -> {
                val results = (state as SearchState.Loaded).results
                Box(modifier = Modifier.weight(1f)) {
                    SearchResults(
                        searchText = viewModel.searchBarText.value,
                        numberOfSearchResults = results.count(),
                        searchResults = results,
                        onOpenProgramme = openProgramme,
                        universityImage = viewModel.universityImage
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            SearchField(
                search = { search() },
                clearSearch = { viewModel.resetSearchResults() },
                title = stringResource(id = R.string.search_field_placeholder),
                searchBarText = viewModel.searchBarText,
                searching = viewModel.searching,
                selectedSchool = viewModel.selectedSchool
            )
        }
    }

    LaunchedEffect(key1 = viewModel.selectedSchool) {
        viewModel.schoolNotSelected.value = viewModel.selectedSchool.value == null
    }
}