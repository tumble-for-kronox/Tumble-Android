package tumble.app.tumble.presentation.screens.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    appBarState: AppBarState,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    elevated: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (elevated) Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) else Modifier
            ),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        TopAppBar(
            title = {
                AnimatedTitleContent(appBarState.title)
            },
            navigationIcon = {
                appBarState.navigationAction?.let { navigationAction ->
                    AnimatedContent(
                        targetState = navigationAction,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(200)) togetherWith
                                    fadeOut(animationSpec = tween(200))
                        },
                        label = "navigation_icon_animation"
                    ) { action ->
                        action()
                    }
                }
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    appBarState.actions?.invoke(this)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            scrollBehavior = scrollBehavior
        )
    }
}

@Composable
private fun AnimatedTitleContent(title: String) {
    AnimatedContent(
        targetState = title,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 50
                )
            ) togetherWith fadeOut(
                animationSpec = tween(durationMillis = 200)
            )
        },
        label = "title_animation"
    ) { animatedTitle ->
        Text(
            text = animatedTitle,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AnimatedActionsRow(actions: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        AnimatedContent(
            targetState = actions,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 250,
                        delayMillis = 100
                    )
                ) togetherWith fadeOut(
                    animationSpec = tween(durationMillis = 150)
                )
            },
            label = "actions_animation"
        ) { animatedActions ->
            animatedActions()
        }
    }
}