package com.socialite.common.ui.extension

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun LazyListScope.contentSpace(space: Dp) {
    item {
        Spacer(modifier = Modifier.size(space))
    }
}