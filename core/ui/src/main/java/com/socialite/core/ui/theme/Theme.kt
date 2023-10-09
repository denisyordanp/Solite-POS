package com.socialite.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.socialite.core.ui.extension.LocalPadding

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = GrayDark,
    primaryVariant = BlackLight,
    onPrimary = GrayLight,
    background = Gray,
    surface = GrayDark,

)

private val LightColorPalette = lightColors(
    primary = Syndicalist,
    primaryVariant = StieglitzSilver,
    onPrimary = SecretPassage,
    background = LightHouse,
    surface = White,
    onSurface = Whiteout,
    onBackground = SnowFlake,
    error = Red,
    onError = TomatoFrog
)

@Composable
fun SolitePOSTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalPadding provides Padding()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
