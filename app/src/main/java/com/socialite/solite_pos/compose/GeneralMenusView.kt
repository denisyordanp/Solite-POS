package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.flow.flow

@Composable
fun GeneralMenusView(
    orderViewModel: OrderViewModel,
    date: String,
    onClicked: (menu: GeneralMenus) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(GeneralMenus.values()) {

            val badgeNumber = if (it != GeneralMenus.ORDERS) {
                flow<Int?> {
                    emit(null)
                }.collectAsState(initial = null)
            } else {
                orderViewModel.getMenuBadge(date).collectAsState(initial = null)
            }

            MenuItemView(
                title = it.title,
                icon = it.icon,
                badgeNumber = if (badgeNumber.value == 0) null else badgeNumber.value,
                onClicked = {
                    onClicked(it)
                }
            )
        }
    }
}
