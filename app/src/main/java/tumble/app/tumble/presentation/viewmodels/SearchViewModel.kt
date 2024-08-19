package tumble.app.tumble.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tumble.app.tumble.datasource.SchoolManager
import tumble.app.tumble.datasource.network.ApiResponse
import tumble.app.tumble.datasource.network.Endpoint
import tumble.app.tumble.datasource.network.kronox.KronoxRepository
import tumble.app.tumble.datasource.preferences.DataStoreManager
import tumble.app.tumble.domain.enums.SearchStatus
import tumble.app.tumble.domain.models.network.NetworkResponse
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.domain.models.presentation.SearchPreviewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kronoxManager: KronoxRepository,
    private val preferenceService: DataStoreManager,
    private val schoolManager: SchoolManager,
): ViewModel() {
    var status by mutableStateOf<SearchStatus>(SearchStatus.INITIAL)
    var programmeSearchResults by mutableStateOf<List<NetworkResponse.Programme>>(emptyList())
    var errorMessageSearch by mutableStateOf<String?>(null)
    //var searchPreviewModel by mutableStateOf<SearchPreviewModel?>(null)
    var schoolNotSelected = mutableStateOf(true)
    var selectedSchool = mutableStateOf<School?>(null)
    var universityImage by mutableStateOf<Int?>(null)
    var searching = mutableStateOf(false)
    var searchBarText = mutableStateOf("")

    private var currentSearchJob: Job? = null

    val schools: List<School> by lazy { schoolManager.getSchools() }

    fun search(query: MutableState<String>, selectedSchoolId: Int){
        status = SearchStatus.LOADING
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {

            // inserting fake data
            if (query.value == "test"){
                val data = fakedata()
                programmeSearchResults = data
                status = SearchStatus.LOADED
                return@launch
            }


            try{
                val endpoint = Endpoint.SearchProgramme(query.value, selectedSchoolId.toString())
                val searchResult: ApiResponse<NetworkResponse.Search> = kronoxManager.getProgramme(endpoint)
                if  (status != SearchStatus.LOADING){
                    return@launch
                }
                parseSearchResults(searchResult)
            } catch (e: Exception){
                if (e is CancellationException){
                    return@launch
                }
            }
        }
    }
     fun resetSearchResults(){
        programmeSearchResults = listOf()
        currentSearchJob?.cancel()
        status = SearchStatus.INITIAL
    }
    private fun parseSearchResults(result: ApiResponse<NetworkResponse.Search> ){
        when(result){
            is ApiResponse.Success -> {
                programmeSearchResults = result.data.items
                status = SearchStatus.LOADED
            }
            is ApiResponse.Error -> {
                status = SearchStatus.INITIAL
            }
            is ApiResponse.Loading -> {}
        }
    }
    override fun onCleared() {
        super.onCleared()
        currentSearchJob?.cancel()
    }
}

// fake data
fun fakedata(): List<NetworkResponse.Programme> {
    val data: MutableList<NetworkResponse.Programme> = mutableListOf()
    for (i in 0..3) {
        val program = NetworkResponse.Programme(
            id = i.toString(),
            title = "testTitle$i",
            subtitle = "testSubtitle$i"
        )
        data.add(program)
    }
    return data.toList()
}