package com.socialite.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round20
import com.socialite.core.ui.extension.size14Normal

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    val bgColor = if (isEnabled) MaterialTheme.colors.primary else MaterialTheme.colors.background
    val textColor = if (isEnabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.round20)
            .background(bgColor)
            .run {
                if (isEnabled)
                    clickable { onClick() }
                else this
            }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    horizontal = MaterialTheme.paddings.medium,
                    vertical = MaterialTheme.paddings.smallMedium
                ),
            text = text,
            style = MaterialTheme.typography.size14Normal,
            color = textColor
        )
    }
}