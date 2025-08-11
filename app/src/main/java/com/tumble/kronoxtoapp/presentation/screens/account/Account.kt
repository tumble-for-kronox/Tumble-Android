package com.tumble.kronoxtoapp.presentation.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.screens.account.login.AccountLogin
import com.tumble.kronoxtoapp.presentation.screens.account.user.profile.UserOverview
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountViewModel
import com.tumble.kronoxtoapp.services.authentication.AuthState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Account(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LaunchedEffect(Unit) {
                    resetTopNavState(
                        setTopNavState,
                        showLogoutDialog = viewModel::showLogoutDialog,
                        navigateToAccountSettings = {
                            navController.navigate(Routes.accountSettings)
                        },
                        authState
                    )
                }
                CustomProgressIndicator()
            }
        }

        is AuthState.Unauthenticated -> {
            LaunchedEffect(Unit) {
                resetTopNavState(
                    setTopNavState,
                    showLogoutDialog = viewModel::showLogoutDialog,
                    navigateToAccountSettings = {
                        navController.navigate(Routes.accountSettings)
                    },
                    authState
                )
            }

            AccountLogin()
        }

        is AuthState.Authenticated -> {
            val user = (authState as AuthState.Authenticated).user
            val isShowingLogoutDialog by viewModel.isShowingLogoutDialog.collectAsState()
            val bookingsState by viewModel.bookingsState.collectAsState()
            val eventsState by viewModel.eventsState.collectAsState()

            LaunchedEffect(Unit) {
                resetTopNavState(
                    setTopNavState,
                    showLogoutDialog = viewModel::showLogoutDialog,
                    navigateToAccountSettings = {
                        navController.navigate(Routes.accountSettings)
                    },
                    authState
                )
            }

            UserOverview(
                user = user,
                schoolName = viewModel.getSchoolName()?.name,
                resetTopNavState = {
                    resetTopNavState(
                        setTopNavState,
                        showLogoutDialog = viewModel::showLogoutDialog,
                        navigateToAccountSettings = {
                            navController.navigate(Routes.accountSettings)
                        },
                        authState
                    )
                },
                bookingsState = bookingsState,
                eventsState = eventsState,
                onClickResource = viewModel::selectBooking,
                onClickEvent = viewModel::selectEvent,
                onConfirmBooking = { resourceId, bookingId ->
                    viewModel.confirmResource(resourceId, bookingId)
                    viewModel.loadUserBookings()
                },
                onUnbookResource = { bookingId ->
                    viewModel.unbookResource(bookingId)
                    viewModel.loadUserBookings()
                },
                onLoadUserEvents = viewModel::loadUserEvents,
                onLoadUserBookings = viewModel::loadUserBookings,
                selectedBooking = viewModel.selectedBooking.collectAsState().value,
                selectedEvent = viewModel.selectedEvent.collectAsState().value,
                onClearSelectedBooking = viewModel::clearSelectedBooking,
                onClearSelectedEvent = viewModel::clearSelectedEvent,
                setTopNavState = setTopNavState,
                navController = navController
            )

            if (isShowingLogoutDialog) {
                LogoutConfirmationDialog(
                    onConfirm = viewModel::logout,
                    onDismiss = viewModel::hideLogoutDialog
                )
            }
        }
    }
}

fun resetTopNavState(
    setTopNavState: (AppBarState) -> Unit,
    showLogoutDialog: () -> Unit,
    navigateToAccountSettings: () -> Unit,
    authState: AuthState
) {
    setTopNavState(
        AppBarState(
            title = "Account",
            actions = {
                when (authState) {
                    is AuthState.Authenticated -> {
                        LogoutButton { showLogoutDialog() }
                    }

                    else -> {}
                }
                SettingsButton { navigateToAccountSettings() }
            }
        )
    )
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    ActionButton(
        imageVector = Icons.AutoMirrored.Filled.Logout,
        onClick = onClick
    )
}

@Composable
private fun SettingsButton(onClick: () -> Unit) {
    ActionButton(
        imageVector = Icons.Default.Settings,
        onClick = onClick
    )
}

@Composable
private fun ActionButton(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.logout_confirm_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Text(
                text = stringResource(R.string.logout_confirm_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            )
        },
        shape = RoundedCornerShape(size = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}