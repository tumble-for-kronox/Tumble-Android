package com.tumble.kronoxtoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumble.kronoxtoapp.domain.models.network.NetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tumble.kronoxtoapp.services.DataStoreService
import com.tumble.kronoxtoapp.services.SchoolService
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.services.authentication.AuthenticationService
import javax.inject.Inject

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

data class LoginFormState(
    val selectedSchool: School? = null,
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
) {
    val isValid: Boolean
        get() = selectedSchool != null && username.isNotBlank() && password.isNotBlank()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val dataStoreService: DataStoreService,
    private val schoolService: SchoolService
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    val schools: List<School> = schoolService.getSchools()

    init {
        initializeSelectedSchool()
    }

    private fun initializeSelectedSchool() {
        viewModelScope.launch {
            dataStoreService.authSchoolId.collect { schoolId ->
                val school = schoolService.getSchoolById(schoolId)
                if (school != null && _formState.value.selectedSchool == null) {
                    _formState.value = _formState.value.copy(selectedSchool = school)
                }
            }
        }
    }

    fun selectSchool(school: School) {
        _formState.value = _formState.value.copy(selectedSchool = school)
        viewModelScope.launch {
            dataStoreService.setAuthSchoolId(school.id)
        }
    }

    fun updateUsername(username: String) {
        _formState.value = _formState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _formState.value = _formState.value.copy(password = password)
    }

    fun togglePasswordVisibility() {
        _formState.value = _formState.value.copy(
            isPasswordVisible = !_formState.value.isPasswordVisible
        )
    }

    fun login() {
        val currentForm = _formState.value
        if (!currentForm.isValid) return

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val userRequest = NetworkRequest.KronoxUserLogin(currentForm.username, currentForm.password)
            authenticationService.loginUser(userRequest)
                .onSuccess {
                    _uiState.value = LoginUiState.Idle
                    _formState.value = LoginFormState(selectedSchool = currentForm.selectedSchool)
                }
                .onFailure { e ->
                    _uiState.value = LoginUiState.Error(
                        e.localizedMessage ?: "Login failed"
                    )
                }
        }
    }

    fun clearError() {
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }
}