package com.fintrack.app.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = FinTrackEmerald,
    onPrimary = FinTrackBlack,
    secondary = FinTrackGold,
    onSecondary = FinTrackBlack,
    tertiary = FinTrackCoral,
    background = FinTrackNavy,
    onBackground = FinTrackTextPrimary,
    surface = FinTrackSurface,
    onSurface = FinTrackTextPrimary,
    surfaceVariant = FinTrackSurfaceHigh,
    onSurfaceVariant = FinTrackTextSecondary,
    outline = FinTrackDivider
)

private val LightColorScheme: ColorScheme = androidx.compose.material3.lightColorScheme(
    primary = FinTrackEmeraldDark,
    onPrimary = Color.White,
    secondary = FinTrackGold,
    onSecondary = FinTrackBlack,
    tertiary = FinTrackCoral,
    background = FinTrackLightBg,
    onBackground = FinTrackLightTextPrimary,
    surface = FinTrackLightSurface,
    onSurface = FinTrackLightTextPrimary,
    surfaceVariant = FinTrackLightSurfaceHigh,
    onSurfaceVariant = FinTrackLightTextSecondary,
    outline = FinTrackLightDivider
)

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = if (darkTheme) FinTrackNavy.toArgb() else FinTrackLightBg.toArgb()
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FinTrackTypography,
        content = content
    )
}
