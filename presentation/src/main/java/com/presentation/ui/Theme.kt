package com.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = LightColors
//        if (!darkTheme) {
//        LightColors
//    } else {
//        DarkColors
//    }
    val view = LocalView.current
    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = AppTypography
    )
}

private val LightColors = lightColorScheme(
    primary = primaryColorLight,
    onPrimary = onPrimaryColorLight,
    surface = surfaceLight,
    background = surfaceLight,
    onSurface = onSurfaceLight,
    tertiary = tertiaryLight
//    primaryContainer = md_theme_light_primaryContainer,
//    onPrimaryContainer = md_theme_light_onPrimaryContainer,
//    secondary = md_theme_light_secondary,
//    onSecondary = md_theme_light_onSecondary,
//    secondaryContainer = md_theme_light_secondaryContainer,
//    onSecondaryContainer = md_theme_light_onSecondaryContainer
)