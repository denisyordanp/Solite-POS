package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round12
import com.socialite.core.ui.extension.round8
import com.socialite.core.ui.extension.size14Normal
import com.socialite.feature.customerorder.R

@Composable
fun CustomerItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.round12)
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.paddings.medium)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = name,
                style = MaterialTheme.typography.size14Normal
            )
            if (isSelected) {
                Surface(
                    color = MaterialTheme.colors.primary,
                    shape = MaterialTheme.shapes.round8
                ) {
                    Icon(
                        modifier = Modifier.padding(MaterialTheme.paddings.extraSmall),
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null,
                    )
                }
            }
        }

    }
}