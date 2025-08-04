package com.tumble.kronoxtoapp.presentation.screens.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountViewModel
import com.tumble.kronoxtoapp.presentation.screens.account.login.AccountLogin
import com.tumble.kronoxtoapp.presentation.screens.account.user.profile.UserOverview
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.sheets.EventDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.sheets.ResourceDetailsSheet
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun Account(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.account)

    val isSigningOut by viewModel.isSigningOut
    val authStatus by viewModel.authStatus.collectAsState()
    val booking by viewModel.booking.collectAsState()
    val event by viewModel.event.collectAsState()

    val topBarState = remember(authStatus, pageTitle) {
        AppBarState(
            title = pageTitle,
            actions = {
                if (authStatus == AccountViewModel.AuthStatus.AUTHORIZED) {
                    SignOutButton { viewModel.openLogOutConfirm() }
                }
                SettingsButton { navController.navigate(Routes.accountSettings) }
            }
        )
    }

    LaunchedEffect(topBarState) {
        setTopNavState(topBarState)
    }

    AccountContent(
        authStatus = authStatus,
        navController = navController
    )

    if (isSigningOut) {
        LogoutConfirmationDialog(
            onConfirm = {
                viewModel.logOut()
                viewModel.closeLogOutConfirm()
            },
            onDismiss = { viewModel.closeLogOutConfirm() }
        )
    }

    ResourceBottomSheet(
        booking = booking,
        onDismiss = {
            setTopNavState(topBarState)
            viewModel.unSetBooking()
        },
        onUnbook = { bookingId ->
            viewModel.unBookResource(bookingId)
            setTopNavState(topBarState)
            viewModel.unSetBooking()
            viewModel.getUserBookingsForSection()
        },
        onConfirm = { resourceId, bookingId ->
            viewModel.confirmResource(resourceId, bookingId)
        },
        setTopNavState = setTopNavState
    )

    EventBottomSheet(
        event = event,
        onDismiss = {
            viewModel.unSetEvent()
            setTopNavState(topBarState)
        },
        setTopNavState = setTopNavState
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun AccountContent(
    authStatus: AccountViewModel.AuthStatus,
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (authStatus) {
            AccountViewModel.AuthStatus.AUTHORIZED -> UserOverview(navController = navController)
            AccountViewModel.AuthStatus.UNAUTHORIZED -> AccountLogin()
            AccountViewModel.AuthStatus.LOADING -> CustomProgressIndicator()
        }
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
                    imageVector = Icons.Outlined.Logout,
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

@Composable
private fun ResourceBottomSheet(
    booking: NetworkResponse.KronoxUserBookingElement?,
    onDismiss: () -> Unit,
    onUnbook: (String) -> Unit,
    onConfirm: (String, String) -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    val slideTransition = slideInVertically(initialOffsetY = { it }) to
            slideOutVertically(targetOffsetY = { it })

    AnimatedVisibility(
        visible = booking != null,
        enter = fadeIn() + slideTransition.first,
        exit = fadeOut() + slideTransition.second
    ) {
        booking?.let {
            ResourceDetailsSheet(
                booking = it,
                onDismiss = onDismiss,
                onUnbook = { onUnbook(it.id) },
                onConfirm = onConfirm,
                setTopNavState = setTopNavState
            )
        }
    }
}

@Composable
private fun EventBottomSheet(
    event: NetworkResponse.AvailableKronoxUserEvent?,
    onDismiss: () -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    val slideTransition = slideInVertically(initialOffsetY = { it }) to
            slideOutVertically(targetOffsetY = { it })

    AnimatedVisibility(
        visible = event != null,
        enter = fadeIn() + slideTransition.first,
        exit = fadeOut() + slideTransition.second
    ) {
        event?.let {
            EventDetailsSheet(
                event = it,
                onDismiss = onDismiss,
                setTopNavState = setTopNavState
            )
        }
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    ActionButton(
        imageVector = Icons.Default.Logout,
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