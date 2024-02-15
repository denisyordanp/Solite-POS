package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.socialite.core.extensions.toKFormat
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round20
import com.socialite.core.ui.extension.size12Normal
import com.socialite.core.ui.extension.size14Normal
import com.socialite.core.ui.extension.spanStyles
import com.socialite.feature.customerorder.R

@Composable
fun BucketWidget(
    modifier: Modifier = Modifier,
    orderCount: Int,
    totalOrder: Long,
    onCLick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(160.dp)
            .clip(MaterialTheme.shapes.round20)
            .background(MaterialTheme.colors.primary)
            .clickable { onCLick() },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.paddings.medium,
                    vertical = MaterialTheme.paddings.smallMedium
                )
        ) {
            val countText = buildAnnotatedString {
                withStyle(MaterialTheme.spanStyles.bold) {
                    append(orderCount.toString())
                }
                append(" ${stringResource(R.string.menu_title)}")
            }
            Text(
                modifier = Modifier.weight(1f),
                text = countText,
                style = MaterialTheme.typography.size12Normal
            )

            val totalText = buildAnnotatedString {
                append(stringResource(id = R.string.idr_title))
                withStyle(MaterialTheme.spanStyles.bold) {
                    append(" ${totalOrder.toKFormat()}")
                }
            }
            Text(
                text = totalText,
                style = MaterialTheme.typography.size14Normal
            )
        }
    }
}