package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BadgeNumber(
    modifier: Modifier = Modifier,
    badge: Int
) {
    Surface(
        modifier = modifier
            .size(15.dp),
        elevation = 4.dp,
        shape = CircleShape,
        color = Color.Red
    ) {
        Box {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = badge.toString(),
                style = MaterialTheme.typography.overline,
                color = Color.White
            )
        }
    }
}
