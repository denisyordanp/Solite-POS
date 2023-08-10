package com.socialite.solite_pos.view.screens.store

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.compose.MenuItemView
import com.socialite.solite_pos.view.ui.MasterMenus

@Composable
fun MasterMenusView(
    onMenuClicked: (menu: MasterMenus) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(MasterMenus.values()) {
            MenuItemView(
                title = it.title,
                onClicked = {
                    onMenuClicked(it)
                }
            )
        }
    }
}
