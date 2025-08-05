package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.url
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import com.tumble.kronoxtoapp.domain.enums.SearchStatus
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.presentation.School
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kronoxManager: KronoxService,
    private val preferenceService: DataStoreService,
    private val schoolService: SchoolService,
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

    val schools: List<School> by lazy { schoolService.getSchools() }

    fun search(query: MutableState<String>, selectedSchoolId: Int){
        status = SearchStatus.LOADING
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {

            try {
                val endpoint = Endpoint.SearchProgramme(query.value, selectedSchoolId.toString())
                Log.d("search", endpoint.url())
                val searchResult: ApiResponse<NetworkResponse.Search> = kronoxManager.getProgramme(endpoint)
                parseSearchResults(searchResult)
            } catch (e: Exception){
                status = SearchStatus.ERROR
                if (e is CancellationException){
                    Log.e("error", errorMessageSearch.toString())
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

    private fun parseSearchResults(result: ApiResponse<NetworkResponse.Search>){
        when(result){
            is ApiResponse.Success -> {
                programmeSearchResults = result.data.items
                status = SearchStatus.LOADED
            }
            is ApiResponse.Error -> {
                status = SearchStatus.ERROR
            }
            else -> {
                status = SearchStatus.LOADING
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentSearchJob?.cancel()
    }
}