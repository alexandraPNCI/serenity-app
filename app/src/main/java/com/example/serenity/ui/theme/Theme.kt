package com.example.serenity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SerenityColorScheme = lightColorScheme(
    primary = SerenityPrimary,
    secondary = SerenityAccent,
    background = SerenityBackground,
    surface = SerenitySurface
)

@Composable
fun SerenityTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SerenityColorScheme,
        typography = Typography(),
        content = content
    )
}


