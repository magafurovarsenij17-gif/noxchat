package com.noxchat.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NoxDarkColors = darkColorScheme(
    primary = AccentViolet,
    secondary = AccentTeal,
    background = NightBase,
    surface = NightSurface,
    surfaceVariant = NightSurfaceHigh,
    outline = NightBorder,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary
)

@Composable
fun NoxChatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NoxDarkColors,
        typography = NoxTypography,
        content = content
    )
}