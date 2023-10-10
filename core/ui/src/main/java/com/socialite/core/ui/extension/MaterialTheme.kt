package com.socialite.core.ui.extension

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.socialite.core.ui.theme.Paddings
import com.socialite.core.ui.theme.SpanStyles

val MaterialTheme.paddings: Paddings
    @Composable
    @ReadOnlyComposable
    get() = LocalPaddings.current

val MaterialTheme.spanStyles: SpanStyles
    @Composable
    @ReadOnlyComposable
    get() = LocalSpanStyles.current