package tumble.app.tumble.presentation.views.account.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.viewmodels.LoginViewModel
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator


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
            .padding(horizontal = 20.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {

                SchoolSelectionMenu(
                    selectedSchool = selectedSchool,
                    onSchoolSelected =   {school ->
                        selectedSchool = school
                        setSchoolForAuth(viewModel1 = viewModel1, selectedSchool = school)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                UsernameField(username)
                PasswordField(password, visiblePassword)

            }
            if(attemptingLogin){
                CustomProgressIndicator()
            }else{
                LoginButton(
                    login = { viewModel.logIn(selectedSchool?.id?: -1, username.value, password.value)},
                    enabled = username.value.isNotBlank() && password.value.isNotBlank() && selectedSchool != null
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
        TextField(value = selectedSchool?.name?: "Select University",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = schoolMenuVisibility)},
        )
        ExposedDropdownMenu(
            expanded = schoolMenuVisibility,
            onDismissRequest = { schoolMenuVisibility = false }) {

            viewModel.schools.forEach { school ->
                DropdownMenuItem(onClick = {
                    schoolMenuVisibility = false
                    onSchoolSelected(school) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = school.name, style = MaterialTheme.typography.body1)
                        if (school == selectedSchool) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

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