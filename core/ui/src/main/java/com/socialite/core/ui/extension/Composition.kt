package com.socialite.core.ui.extension

import androidx.compose.runtime.staticCompositionLocalOf
import com.socialite.core.ui.theme.Paddings
import com.socialite.core.ui.theme.SpanStyles

internal val LocalPaddings = staticCompositionLocalOf { Paddings() }
internal val LocalSpanStyles = staticCompositionLocalOf { SpanStyles() }