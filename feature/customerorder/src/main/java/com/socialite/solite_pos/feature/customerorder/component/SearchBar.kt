package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round20
import com.socialite.core.ui.theme.SolitePOSTheme
import com.socialite.feature.customerorder.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (value: String) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(MaterialTheme.shapes.round20)
            .background(MaterialTheme.colors.surface)
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = MaterialTheme.paddings.smallMedium)
                .weight(1f)
        ) {
            if (value.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeholder,
                    style = TextStyle.Default.copy(
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 12.sp
                    )
                )
            } else {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange
                )
            }
        }
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.small))
        Icon(
            modifier = Modifier.padding(end = MaterialTheme.paddings.smallMedium),
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SolitePOSTheme {
        SearchBar(
            value = "",
            placeholder = "Placeholder",
            onValueChange = {}
        )
    }
}