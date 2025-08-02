package tumble.app.tumble.presentation.screens.account.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.extensions.presentation.noRippleClickable
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.viewmodels.LoginViewModel
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AccountLogin(
    viewModel: AccountViewModel = hiltViewModel(),
    viewModel1: LoginViewModel = hiltViewModel()
){
    val username = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val visiblePassword = remember {
        mutableStateOf(false)
    }

    var selectedSchool by remember {
        mutableStateOf<School?>(viewModel1.getSchoolName())
    }

    val attemptingLogin by viewModel.attemptingLogin.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            LoginHeader()
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                SchoolSelectionMenu(
                    selectedSchool = selectedSchool,
                    onSchoolSelected = { school ->
                        selectedSchool = school
                        setSchoolForAuth(viewModel1 = viewModel1, selectedSchool = school)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                UsernameField(username)
                Spacer(modifier = Modifier.height(10.dp))
                PasswordField(password, visiblePassword)
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (attemptingLogin) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomProgressIndicator()
                }
            } else {
                LoginButton(
                    login = { viewModel.login(username.value, password.value)},
                    enabled = username.value.isNotBlank() && password.value.isNotBlank() && selectedSchool != null
                )
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
    viewModel: LoginViewModel = hiltViewModel()
){
    var schoolMenuVisibility by remember {
        mutableStateOf<Boolean>(false)
    }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selectedSchool?.name ?: "Select University",
                        color = if (selectedSchool != null) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
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
            viewModel.schools.forEach { school ->
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

fun setSchoolForAuth(
    selectedSchool: School?,
    viewModel1: LoginViewModel
){
    selectedSchool?.let {
        viewModel1.setDefaultAuthSchool(schoolId = it.id)
    }
}