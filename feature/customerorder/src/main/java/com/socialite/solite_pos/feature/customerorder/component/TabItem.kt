package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.socialite.core.ui.extension.captionBold
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.tab

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface
    val textColor =
        if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.onPrimary

    Box(
        modifier = modifier
            .height(30.dp)
            .clip(MaterialTheme.shapes.tab)
            .background(bgColor)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = MaterialTheme.paddings.medium,
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.captionBold.copy(
                color = textColor
            ),
            textAlign = TextAlign.Center
        )
    }
}