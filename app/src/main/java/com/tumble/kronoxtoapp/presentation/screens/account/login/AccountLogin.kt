package com.tumble.kronoxtoapp.presentation.screens.account.login

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.viewmodels.LoginUiState
import com.tumble.kronoxtoapp.presentation.viewmodels.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AccountLogin(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            LoginHeader()

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                SchoolSelectionMenu(
                    selectedSchool = formState.selectedSchool,
                    onSchoolSelected = viewModel::selectSchool,
                    schools = viewModel.schools
                )

                Spacer(modifier = Modifier.height(20.dp))

                UsernameField(
                    username = formState.username,
                    onUsernameChange = viewModel::updateUsername
                )

                Spacer(modifier = Modifier.height(10.dp))

                PasswordField(
                    password = formState.password,
                    onPasswordChange = viewModel::updatePassword,
                    isPasswordVisible = formState.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                is LoginUiState.Loading -> {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomProgressIndicator()
                    }
                }

                is LoginUiState.Error -> {
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = (uiState as LoginUiState.Error).message,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LoginButton(
                            onLogin = viewModel::login,
                            enabled = formState.isValid
                        )
                    }
                }

                else -> {
                    LoginButton(
                        onLogin = viewModel::login,
                        enabled = formState.isValid
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolSelectionMenu(
    selectedSchool: School?,
    onSchoolSelected: (School) -> Unit,
    schools: List<School>
) {
    var schoolMenuVisibility by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = schoolMenuVisibility,
        onExpandedChange = { schoolMenuVisibility = !schoolMenuVisibility }
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.Transparent,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selectedSchool?.name ?: "Select University",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (selectedSchool != null) {
                            FontWeight.Medium
                        } else {
                            FontWeight.Normal
                        }
                    )
                }
                Icon(
                    imageVector = if (schoolMenuVisibility) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        ExposedDropdownMenu(
            expanded = schoolMenuVisibility,
            onDismissRequest = { schoolMenuVisibility = false }
        ) {
            schools.forEach { school ->
                DropdownMenuItem(
                    onClick = {
                        schoolMenuVisibility = false
                        onSchoolSelected(school)
                    },
                    text = {
                        Text(text = school.name, style = MaterialTheme.typography.bodyMedium)
                    },
                )
            }
        }
    }
}