package tumble.app.tumble.presentation.views.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tumble.app.tumble.R
import tumble.app.tumble.domain.enums.SearchStatus
import tumble.app.tumble.domain.models.presentation.SearchPreviewModel
import tumble.app.tumble.observables.AppController
import tumble.app.tumble.presentation.navigation.UriBuilder
import tumble.app.tumble.presentation.viewmodels.ParentViewModel
import tumble.app.tumble.presentation.viewmodels.SearchViewModel
import tumble.app.tumble.presentation.views.general.Info
import tumble.app.tumble.presentation.views.navigation.AppBarState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Search(
    viewModel: SearchViewModel = hiltViewModel(),
    parentViewModel: ParentViewModel = hiltViewModel(),
    navController: NavController = rememberNavController(),
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.search)

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

     fun openProgramme(programmeId: String){
        viewModel.selectedSchool.value?.id?.let {
            selectedSchoolId ->
            AppController.shared.searchPreview = SearchPreviewModel(scheduleId = programmeId, schoolId = selectedSchoolId.toString())
            navController.navigate(UriBuilder.buildSearchDetailsUri(programmeId).toUri())
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

        when(viewModel.status){
            SearchStatus.INITIAL -> { SearchInfo(schools = viewModel.schools, selectedSchool =  viewModel.selectedSchool)}
            SearchStatus.LOADING -> { LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) }
            SearchStatus.LOADED -> {
                Box(modifier = Modifier.weight(1f)) {
                    SearchResults(
                        searchText = viewModel.searchBarText.value,
                        numberOfSearchResults = viewModel.programmeSearchResults.count(),
                        searchResults = viewModel.programmeSearchResults,
                        onOpenProgramme = { openProgramme(it) },
                        universityImage = viewModel.universityImage
                    )
                }
            }
            SearchStatus.ERROR -> { Info(title = stringResource(id = R.string.error_something_wrong), image = null) }
            SearchStatus.EMPTY -> { Info(title = stringResource(id = R.string.error_empty_schedule), image = null) }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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