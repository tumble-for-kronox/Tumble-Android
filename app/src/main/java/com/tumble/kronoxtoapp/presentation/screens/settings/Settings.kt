package com.tumble.kronoxtoapp.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.tumble.kronoxtoapp.BuildConfig
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.navigation.Routes
import com.tumble.kronoxtoapp.presentation.screens.settings.buttons.SettingsNavigationButton
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsList
import com.tumble.kronoxtoapp.presentation.screens.settings.list.SettingsListGroup
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState

@Composable
fun SettingsScreen(
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.accountSettings)
    val backTitle = stringResource(R.string.account)
    var showShareSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val appVersion = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        BuildConfig.VERSION_NAME
    }

    val externalNav = { uri:String ->
        startActivity(
            context,
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            ),
            null)
    }
    LaunchedEffect(key1 = true) {
        resetNavState(
            setTopNavState,
            pageTitle,
            backTitle,
            navController
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SettingsList {
            SettingsListGroup(title = "Account") {
                SettingsNavigationButton(
                    title = "Preferences",
                    leadingIcon = Icons.Default.Checklist,
                    trailingIcon = Icons.Default.ChevronRight,
                    action = { navController.navigate(Routes.accountSettingsPreferences) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                SettingsNavigationButton(
                    title = "Bookmarks",
                    leadingIcon = Icons.Default.Bookmark,
                    trailingIcon = Icons.Default.ChevronRight,
                    action = { navController.navigate(Routes.accountSettingsBookmarks) }
                )
            }

            SettingsListGroup(title = "Support") {
                SettingsNavigationButton(
                    title = stringResource(R.string.review_app),
                    leadingIcon = Icons.Default.Star,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { externalNav("market://details?id=com.tumble.kronoxtoapp") }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.share_feedback),
                    leadingIcon = Icons.Default.Email,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = {
                        externalNav("mailto:support@tumbleforkronox.com")
                    }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.share_app),
                    leadingIcon = Icons.Default.Share,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { showShareSheet = true }
                )
            }

            SettingsListGroup(title = "Community") {
                SettingsNavigationButton(
                    title = stringResource(R.string.github),
                    leadingIcon = Icons.Default.Code,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { externalNav("https://github.com/tumble-for-kronox/Tumble-Android") }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.discord),
                    leadingIcon = Icons.Default.Forum,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { externalNav("https://discord.gg/g4QQFuwRFT") }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                SettingsNavigationButton(
                    title = "Buy Me a Coffee",
                    leadingIcon = Icons.Default.Coffee,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { externalNav("https://buymeacoffee.com/defaultdino") }
                )
            }

            // Version at bottom without group
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tumble, Android v.$appVersion",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

    AnimatedVisibility(
        visible = showShareSheet,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        ShareSheet(
            context = LocalContext.current,
            onDismiss = {
                showShareSheet = false
                resetNavState(
                    setTopNavState,
                    pageTitle,
                    backTitle,
                    navController
                )
            },
            setTopNavState = setTopNavState
        )
    }
}

private fun resetNavState(
    setTopNavState: (AppBarState) -> Unit,
    pageTitle: String,
    backTitle: String,
    navController: NavController,
) {
    setTopNavState(
        AppBarState(
            title = pageTitle,
            navigationAction = {
                BackButton(label = backTitle) {
                    navController.popBackStack()
                }
            }
        )
    )
}