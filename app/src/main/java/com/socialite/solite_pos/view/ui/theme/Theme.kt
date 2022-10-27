package com.socialite.solite_pos.view.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = GrayDark,
    primaryVariant = BlackLight,
    onPrimary = GrayLight,
    background = Gray,
    surface = GrayDark,

)

private val LightColorPalette = lightColors(
    primary = Yellow,
    primaryVariant = YellowDark,
    onPrimary = GrayDark,
    background = GrayLight,
    surface = Color.White
)

@Composable
fun SolitePOSTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
