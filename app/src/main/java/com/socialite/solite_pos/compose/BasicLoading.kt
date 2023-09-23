package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center),
            color = MaterialTheme.colors.primary,
            strokeWidth = 4.dp
        )
    }
}
