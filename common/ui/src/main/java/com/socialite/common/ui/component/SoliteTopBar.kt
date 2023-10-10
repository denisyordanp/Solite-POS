package com.socialite.common.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.common.ui.R
import com.socialite.core.ui.extension.paddings

@Composable
fun SoliteTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 1.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(65.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .padding(start = MaterialTheme.paddings.small)
                    .size(30.dp),
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.paddings.small))

            content()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SoliteContent {
        SoliteTopBar(
            modifier = it,
            onBackClick = {},
            content = {}
        )
    }
}