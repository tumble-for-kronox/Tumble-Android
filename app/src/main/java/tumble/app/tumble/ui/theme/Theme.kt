package tumble.app.tumble.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import tumble.app.tumble.domain.enums.Types.AppearanceType

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = SecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    onPrimary = OnPrimary,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    error = Danger,
    tertiary = Info
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = SecondaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    onPrimary = OnPrimary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    error = Danger,
    tertiary = Info
)

@Composable
fun TumbleTheme(
    userPreferences: AppearanceType,
    content: @Composable () -> Unit
) {
    val darkTheme = when (userPreferences) {
        AppearanceType.DARK -> true
        AppearanceType.LIGHT -> false
        AppearanceType.AUTOMATIC -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = colorScheme.primary)

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        content = content
    )
}
