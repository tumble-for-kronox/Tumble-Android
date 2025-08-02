package tumble.app.tumble.presentation.screens.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.screens.account.Login.AccountLogin
import tumble.app.tumble.presentation.screens.account.User.ProfileSection.UserOverview
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Sheets.EventDetailsSheet
import tumble.app.tumble.presentation.screens.account.User.ResourceSection.Booking.Sheets.ResourceDetailsSheet
import tumble.app.tumble.presentation.screens.general.CustomProgressIndicator
import tumble.app.tumble.presentation.screens.navigation.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.account)

    val isSigningOut by viewModel.isSigningOut

    val authStatus by viewModel.authStatus.collectAsState()
    val booking = viewModel.booking.collectAsState()
    val event = viewModel.event.collectAsState()

    rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val setTopBar = {
        setTopNavState(
        AppBarState(
            title = pageTitle,
            actions = {
                if (authStatus == AccountViewModel.AuthStatus.AUTHORIZED) {
                    SignOutButton { viewModel.openLogOutConfirm() }
                }
                SettingsButton {
                    navController.navigate(Routes.accountSettings)
                }
            }
        )
    )}

    LaunchedEffect(key1 = true) {
        setTopBar()
    }

    Box (modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when(authStatus){
            AccountViewModel.AuthStatus.AUTHORIZED -> UserOverview(navController = navController)
            AccountViewModel.AuthStatus.UNAUTHORIZED -> AccountLogin()
            AccountViewModel.AuthStatus.LOADING -> CustomProgressIndicator()
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
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.closeLogOutConfirm()
                }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.logout_confirm),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }

    AnimatedVisibility(
        visible = booking.value != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        booking.value?.let {
            ResourceDetailsSheet(
                it,
                {
                    setTopBar()
                    viewModel.unSetBooking()
                },
                { viewModel.unBookResource(it.id) },
                setTopNavState
            )
        }
    }
    AnimatedVisibility(
        visible = booking.value != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        event.value?.let {
            EventDetailsSheet(
                it,
                {
                    viewModel.unSetEvent()
                    setTopBar()
                },
                setTopNavState
            )
        }
    }
}


@Composable
fun SignOutButton(showConfirmationDialog: () -> Unit){
    IconButton(onClick = { showConfirmationDialog() }) {
        Icon(imageVector = Icons.Default.Logout,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsButton(onClick: () -> Unit){
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}