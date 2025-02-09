package tumble.app.tumble.presentation.views.Settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.BackButton
import tumble.app.tumble.presentation.navigation.Routes
import tumble.app.tumble.presentation.viewmodels.SettingsViewModel
import tumble.app.tumble.presentation.views.Settings.Buttons.SettingsNavigationButton
import tumble.app.tumble.presentation.views.Settings.List.SettingsList
import tumble.app.tumble.presentation.views.Settings.List.SettingsListGroup
import tumble.app.tumble.presentation.views.navigation.AppBarState
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.accountSettings)
    val backTitle = stringResource(R.string.account)
    val appearance = viewModel.appearance.collectAsState()
    var showShareSheet by remember { mutableStateOf(false) }

    val currentLocale = Locale.getDefault().displayLanguage
    val appVersion = "1.0.0" // Replace with actual version retrieval logic
    val context = LocalContext.current

    val externalNav = {uri:String ->
        startActivity(
            context,
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            ),
            null)
    }
    LaunchedEffect(key1 = true) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        SettingsList {
            SettingsListGroup {
                SettingsNavigationButton(
                    title = stringResource(R.string.preferences),
                    leadingIcon = Icons.Default.Checklist,
                    action = { navController.navigate(Routes.accountSettingsPreferences) }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.bookmark),
                    leadingIcon = Icons.Default.Bookmark,
                    action = { navController.navigate(Routes.accountSettingsBookmarks) }
                )
            }
            SettingsListGroup {
                SettingsNavigationButton(
                    title = stringResource(R.string.review_app),
                    leadingIcon = Icons.Default.Star,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { externalNav("market://details?id=com.tumble.kronoxtoapp") }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.share_feedback),
                    leadingIcon = Icons.Default.Email,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = {  }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.share_app),
                    leadingIcon = Icons.Default.Share,
                    trailingIcon = Icons.Default.ArrowOutward,
                    action = { showShareSheet = true }
                )
            }
            SettingsListGroup {
                SettingsNavigationButton(
                    title = stringResource(R.string.github),
                    leadingIcon = Icons.Default.Code,
                    action = { externalNav("https://github.com/tumble-for-kronox/Tumble-Android") }
                )
                SettingsNavigationButton(
                    title = stringResource(R.string.discord),
                    leadingIcon = Icons.Default.Forum,
                    action = { externalNav("https://discord.gg/g4QQFuwRFT") }
                )
            }

            if (appVersion != null) {
                SettingsListGroup {
                    Text(
                        text = "Tumble, Android v.$appVersion",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = showShareSheet,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        ShareSheet(context = LocalContext.current) {
            showShareSheet = false
        }
    }
}