package com.socialite.solite_pos.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoadingView(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    content()
    if (isLoading) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.surface.copy(
                        0.7f
                    )
                )
                .fillMaxSize()
                .clickable(
                    indication = null, // disable ripple effect
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { }
                ),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = MaterialTheme.colors.primary,
                strokeWidth = 4.dp
            )
        }
    }
}
