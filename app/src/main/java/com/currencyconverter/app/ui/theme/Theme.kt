package com.currencyconverter.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3F3F1),
    onPrimaryContainer = Ink,
    secondary = Gold,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFFFE7B8),
    onSecondaryContainer = Ink,
    tertiary = Teal,
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Color(0xFF3F5456),
    error = ErrorRed,
    onError = Color.White,
    outline = Color(0xFFB7C6C5)
)

private val DarkColors = darkColorScheme(
    primary = TealLight,
    onPrimary = Night,
    primaryContainer = TealDark,
    onPrimaryContainer = Color.White,
    secondary = Gold,
    onSecondary = Night,
    secondaryContainer = Color(0xFF5A3F0B),
    onSecondaryContainer = Color(0xFFFFE7B8),
    tertiary = Teal,
    background = Night,
    onBackground = Color(0xFFE7F4F3),
    surface = NightCard,
    onSurface = Color(0xFFE7F4F3),
    surfaceVariant = Color(0xFF1A3336),
    onSurfaceVariant = Color(0xFFB7C6C5),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    outline = Color(0xFF3F5456)
)

@Composable
fun CurrencyConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
