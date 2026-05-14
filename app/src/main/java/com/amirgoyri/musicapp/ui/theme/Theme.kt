package com.amirgoyri.musicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    background = PurpleBackground,
    surface = androidx.compose.ui.graphics.Color.White,
    onBackground = NavyDark,
    onSurface = NavyDark,
)

@Composable
fun AGoyriMusicAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
