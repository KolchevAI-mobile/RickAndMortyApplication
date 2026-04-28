package com.example.rickandmortyapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppDarkColorScheme: ColorScheme = darkColorScheme(
    primary = PortalGreen,
    onPrimary = Color(0xFF0A0F18),
    primaryContainer = SurfaceCard,
    onPrimaryContainer = TextPrimary,
    secondary = NeonCyan,
    onSecondary = Color(0xFF0A0F18),
    secondaryContainer = SurfaceCardElevated,
    onSecondaryContainer = TextPrimary,
    tertiary = PlasmaPink,
    onTertiary = Color(0xFF0A0F18),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF0A0F18),
    background = VoidBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceOutline
)

@Composable
fun RickAndMortyTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val context = LocalContext.current
    SideEffect {
        val window = (context as? Activity)?.window
        if (window != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                @Suppress("DEPRECATION")
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                @Suppress("DEPRECATION")
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = RickAndMortyTypography,
        shapes = RickAndMortyShapes,
        content = content
    )
}
