package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.socialite.common.ui.component.SoliteTopBar
import com.socialite.core.ui.extension.paddings

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    searchValue: String,
    onSearchValueChange: (value: String) -> Unit,
    searchPlaceHolder: String,
    onBackClick: () -> Unit
) {
    SoliteTopBar(
        modifier = modifier,
        onBackClick = onBackClick,
        content = {
            SearchBar(
                modifier = Modifier
                    .padding(end = MaterialTheme.paddings.medium)
                    .weight(1f),
                value = searchValue,
                placeholder = searchPlaceHolder,
                onValueChange = onSearchValueChange
            )
        }
    )
}