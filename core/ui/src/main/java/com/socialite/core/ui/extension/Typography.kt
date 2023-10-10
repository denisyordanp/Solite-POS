package com.socialite.core.ui.extension

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography.size16Normal
    @Composable
    @ReadOnlyComposable
    get() = default.copy(
        fontSize = 16.sp,
        color = MaterialTheme.colors.onPrimary,
    )

val Typography.size14Bold
    @Composable
    @ReadOnlyComposable
    get() = size14Normal.copy(
        fontWeight = FontWeight.Bold
    )

val Typography.size14Normal
    @Composable
    @ReadOnlyComposable
    get() = default.copy(
        fontSize = 14.sp,
    )

val Typography.size10Normal
    @Composable
    @ReadOnlyComposable
    get() = default.copy(
        fontSize = 10.sp,
    )

val Typography.size12Normal
    @Composable
    @ReadOnlyComposable
    get() = default.copy(
        fontSize = 12.sp,
    )

val Typography.size12Bold
    @Composable
    @ReadOnlyComposable
    get() = size12Normal.copy(
        fontWeight = FontWeight.Bold,
    )

val Typography.size24Bold
    @Composable
    @ReadOnlyComposable
    get() = default.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )

val Typography.size14SemiBold
    @Composable
    @ReadOnlyComposable
    get() = size14Normal.copy(
        fontWeight = FontWeight.SemiBold,
    )

private val Typography.default
    @Composable
    @ReadOnlyComposable
    get() = TextStyle.Default.copy(
        color = MaterialTheme.colors.onPrimary,
    )