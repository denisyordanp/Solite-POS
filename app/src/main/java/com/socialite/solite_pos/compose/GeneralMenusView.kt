package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus

@Composable
fun GeneralMenusView(
    onClicked: (menu: GeneralMenus) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(GeneralMenus.values()) {
            MenuItemView(
                title = it.title,
                icon = it.icon,
                onClicked = {
                    onClicked(it)
                }
            )
        }
    }
}
