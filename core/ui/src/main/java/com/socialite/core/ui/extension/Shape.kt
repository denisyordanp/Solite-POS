package com.socialite.core.ui.extension

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes.round20: RoundedCornerShape
    get() = RoundedCornerShape(20.dp)

val Shapes.round15: RoundedCornerShape
    get() = RoundedCornerShape(15.dp)

val Shapes.round12: RoundedCornerShape
    get() = RoundedCornerShape(12.dp)

val Shapes.round8: RoundedCornerShape
    get() = RoundedCornerShape(8.dp)

val Shapes.bottomSheet: RoundedCornerShape
    get() = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)