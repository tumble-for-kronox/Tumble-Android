package tumble.app.tumble.presentation.views.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.views.account.Login.AccountLogin
import tumble.app.tumble.presentation.views.account.User.ProfileSection.UserOverview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController
) {

    var isSigningOut by viewModel.isSigningOut

    val authStatus by viewModel.authStatus.collectAsState()

    Box (modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when(authStatus){
            AccountViewModel.AuthStatus.AUTHORIZED -> UserOverview(navController = navController)
            AccountViewModel.AuthStatus.UNAUTHORIZED -> AccountLogin()
        }
    }

    if(isSigningOut){
        AlertDialog(
            onDismissRequest = { viewModel.closeLogOutConfirm() },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logOut()
                    viewModel.closeLogOutConfirm()
                }) {
                    Text(text = "yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeLogOutConfirm() }) {
                    Text(text = "cancel")
                }
            },
            text = {
                Text(text = "coonfirm")
            }
        )
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