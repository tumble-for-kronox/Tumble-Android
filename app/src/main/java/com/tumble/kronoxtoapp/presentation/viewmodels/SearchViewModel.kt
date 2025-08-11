package com.tumble.kronoxtoapp.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.services.kronox.ApiResponse
import com.tumble.kronoxtoapp.services.kronox.Endpoint
import com.tumble.kronoxtoapp.services.kronox.KronoxService
import com.tumble.kronoxtoapp.services.kronox.url
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SearchState {
    data object Initial : SearchState()
    data object Loading : SearchState()
    data class Loaded(val results: List<NetworkResponse.Programme>) : SearchState()
    data class Error(val errorMessage: String) : SearchState()
    data object Empty : SearchState()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kronoxManager: KronoxService,
    private val schoolService: SchoolService,
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    var schoolNotSelected = mutableStateOf(true)
    var selectedSchool = mutableStateOf<School?>(null)
    var universityImage by mutableStateOf<Int?>(null)
    var searching = mutableStateOf(false)
    var searchBarText = mutableStateOf("")

    private var currentSearchJob: Job? = null

    val schools: List<School> by lazy { schoolService.getSchools() }

    fun search(query: MutableState<String>, selectedSchoolId: Int) {
        _state.value = SearchState.Loading
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {

            try {
                val endpoint = Endpoint.SearchProgramme(query.value, selectedSchoolId.toString())
                Log.d("search", endpoint.url())
                val searchResult: ApiResponse<NetworkResponse.Search> =
                    kronoxManager.getProgramme(endpoint)
                parseSearchResults(searchResult)
            } catch (e: Exception) {
                _state.value = SearchState.Error(
                    errorMessage = e.localizedMessage ?: "An unknown error occured"
                )
                if (e is CancellationException) {
                    Log.e("error", e.localizedMessage ?: "Unknown error")
                    return@launch
                }
            }
        }
    }

    fun resetSearchResults() {
        currentSearchJob?.cancel()
        _state.value = SearchState.Initial
    }

    private fun parseSearchResults(result: ApiResponse<NetworkResponse.Search>) {
        when (result) {
            is ApiResponse.Success -> {
                val results = result.data.items
                if (result.data.items.isEmpty()) {
                    _state.value = SearchState.Empty
                    return
                }
                _state.value = SearchState.Loaded(results = results)
            }

            is ApiResponse.Error -> {
                val errorMessage = (result).errorMessage
                _state.value = SearchState.Error(errorMessage)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentSearchJob?.cancel()
    }
}