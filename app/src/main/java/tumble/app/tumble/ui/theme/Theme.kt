package tumble.app.tumble.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import tumble.app.tumble.data.repository.preferences.DataStoreManager
import tumble.app.tumble.domain.enums.Types.AppearanceType

private val DarkColorPalette = darkColors(
    primary = Primary,
    secondary = SecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    onPrimary = OnPrimary,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    primaryVariant = Primary
)

private val LightColorPalette = lightColors(
    primary = Primary,
    secondary = SecondaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    onPrimary = OnPrimary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    primaryVariant = Primary
)

@Composable
fun TumbleTheme(darkTheme: Boolean = isSystemInDarkTheme(),userPreferences: AppearanceType , content: @Composable () -> Unit) {

    val colors = when(userPreferences){
        AppearanceType.DARK -> {DarkColorPalette}
        AppearanceType.LIGHT -> {LightColorPalette}
        AppearanceType.AUTOMATIC -> {
            if (darkTheme) {
                DarkColorPalette
            } else {
                LightColorPalette
            }
        }
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Primary)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}