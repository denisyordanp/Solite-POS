package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.data.source.local.entity.helper.MenuBadge
import com.socialite.solite_pos.view.ui.GeneralMenus

@Composable
fun GeneralMenusView(
    badges: List<MenuBadge>,
    onClicked: (menu: GeneralMenus) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(badges) {
            MenuItemView(
                title = it.menu.title,
                icon = it.menu.icon,
                badgeNumber = it.badge,
                onClicked = {
                    onClicked(it.menu)
                }
            )
        }
    }
}
