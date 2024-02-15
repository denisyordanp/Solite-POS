package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round15
import com.socialite.core.ui.theme.SolitePOSTheme
import com.socialite.feature.customerorder.R

@Composable
fun CircleButtonIcon(
    modifier: Modifier = Modifier,
    iconResource: Int,
    iconColor: Color,
    backgroundColor: Color,
    onCLick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.round15
            )
            .size(25.dp),
        onClick = onCLick
    ) {
        Icon(
            modifier = Modifier
                .padding(MaterialTheme.paddings.extraSmall),
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = iconColor
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SolitePOSTheme {
        CircleButtonIcon(
            iconResource = R.drawable.ic_minus,
            backgroundColor = MaterialTheme.colors.secondary,
            iconColor = MaterialTheme.colors.background,
            onCLick = {}
        )
    }
}