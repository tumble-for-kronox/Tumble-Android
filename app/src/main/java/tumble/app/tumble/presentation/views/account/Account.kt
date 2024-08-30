package tumble.app.tumble.presentation.views.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.views.account.Login.AccountLogin
import tumble.app.tumble.presentation.views.account.User.ProfileSection.UserOverview

@Composable
fun Account(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController
) {

    var isSigningOut by remember {
        mutableStateOf(false)
    }

    val authStatus by viewModel.authStatus.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "account") },
                actions = {
                    Row {
                        if(authStatus == AccountViewModel.AuthStatus.AUTHORIZED){
                            SignOutButton{ isSigningOut = true}
                        }
                        SettingsButton {
                            navController.navigate(Routes.accountSettings)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box (modifier = Modifier
            .fillMaxSize()
            .padding(padding),
            contentAlignment = Alignment.Center
        ){
            when(authStatus){
                AccountViewModel.AuthStatus.AUTHORIZED -> UserOverview(navController = navController)
                AccountViewModel.AuthStatus.UNAUTHORIZED -> AccountLogin()
            }
        }

        if(isSigningOut){
            AlertDialog(
                onDismissRequest = { isSigningOut = false },
                confirmButton = { 
                    TextButton(onClick = {
                        viewModel.logOut()
                        isSigningOut = false
                    }) {
                        Text(text = "yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isSigningOut = false }) {
                        Text(text = "cancel")
                    }
                },
                text = {
                    Text(text = "coonfirm")
                }
            )
        }
    }
}


@Composable
fun SignOutButton(showConfirmationDialog: () -> Unit){
    IconButton(onClick = { showConfirmationDialog() }) {
        Icon(imageVector = Icons.Default.ExitToApp,
            contentDescription = null,
            modifier = Modifier.size(24.dp))

    }
}

@Composable
fun SettingsButton(onClick: () -> Unit){
    IconButton(onClick = { onClick() }) {
        Icon(imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.size(24.dp))
    }
}