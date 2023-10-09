package com.socialite.core.ui.extension

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography.body1Normal
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.body1.copy(
        color = MaterialTheme.colors.onPrimary,
    )

val Typography.body2Bold
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.body2.copy(
        color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.Bold
    )

val Typography.overLineNormal
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.overline.copy(
        color = MaterialTheme.colors.onPrimary,
    )

val Typography.defaultH5
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.typography.h5.copy(
        color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.Bold
    )

val Typography.mainMenu
    @Composable
    @ReadOnlyComposable
    get() = TextStyle.Default.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.onPrimary,
    )