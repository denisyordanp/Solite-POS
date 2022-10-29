package com.socialite.solite_pos.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.view.ui.DropdownItem

fun LazyListScope.basicDropdown(
    isExpanded: Boolean,
    @StringRes title: Int,
    selectedItem: String?,
    isEnable: Boolean = true,
    items: List<DropdownItem>,
    onHeaderClicked: () -> Unit,
    onSelectedItem: (DropdownItem) -> Unit
) {
    item {
        DropdownHeader(
            isExpanded = isExpanded,
            title = stringResource(id = title),
            selectedItem = selectedItem,
            isEnable = isEnable,
            onHeaderClicked = {
                if (isEnable) onHeaderClicked()
            }
        )
    }

    if (isExpanded) {
        items(items) {
            DropdownItem(
                name = it.name,
                onItemClicked = {
                    onSelectedItem(it)
                }
            )
        }
    }
}

@Composable
private fun DropdownHeader(
    isExpanded: Boolean,
    title: String,
    selectedItem: String?,
    isEnable: Boolean,
    onHeaderClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .clickable {
                onHeaderClicked()
            }
            .padding(24.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = selectedItem ?: title,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        if (isEnable) {
            Icon(
                painter = painterResource(
                    id = if (isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
                ),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun DropdownItem(
    name: String,
    onItemClicked: () -> Unit
) {
    Spacer(modifier = Modifier.height(2.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .clickable {
                onItemClicked()
            }
            .padding(16.dp),
        text = name,
        style = MaterialTheme.typography.body2
    )
}
