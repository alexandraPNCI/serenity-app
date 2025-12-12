package com.example.serenity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PastelColorScheme = lightColorScheme(
    primary = PastelPink,
    secondary = PastelPurple,
    tertiary = PastelBlue,
    background = Color(0xFFFFF7FB),
    surface = Color(0xFFFFF7FB),
    onSurface = TextDark,
    onBackground = TextDark
)

@Composable
fun SerenityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography,
        content = content
    )
}

