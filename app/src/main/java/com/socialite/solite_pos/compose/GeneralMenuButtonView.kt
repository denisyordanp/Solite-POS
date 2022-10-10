package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
fun GeneralMenuButtonView(
    modifier: Modifier,
    onMenuClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clickable { onMenuClicked() }
            .shadow(
                elevation = 4.dp,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_menus),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = null
        )
    }
}
