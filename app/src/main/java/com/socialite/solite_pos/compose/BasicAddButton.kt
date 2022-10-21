package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R

@Composable
fun BasicAddButton(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(24.dp)
            .size(50.dp)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
            .clickable { onAddClicked() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_add),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = null
        )
    }
}
