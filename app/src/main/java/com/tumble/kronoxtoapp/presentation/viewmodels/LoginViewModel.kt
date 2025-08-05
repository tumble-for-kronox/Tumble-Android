package com.tumble.kronoxtoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.domain.models.presentation.School
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreService: DataStoreService,
    private val schoolService: SchoolService,
    private val authenticationService: AuthenticationService
): ViewModel(){

    private val _authSchoolId = MutableStateFlow(-1)
    val authSchoolId: StateFlow<Int> = _authSchoolId

    val schools: List<School> = schoolService.getSchools()

    init {
        setupDatePublishers()
    }

    private fun setupDatePublishers(){
        viewModelScope.launch {
            dataStoreService.authSchoolId.collect{ id ->
                _authSchoolId.value = id
            }
        }
    }

    fun setDefaultAuthSchool(schoolId: Int){
        viewModelScope.launch {
            dataStoreService.setAuthSchoolId(id = schoolId)
        }
    }

    fun getSchoolName(): School?{
        return schoolService.getSchoolById(authSchoolId.value)
    }
}