package com.socialite.solite_pos.view.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.StoreMenuItem
import com.socialite.solite_pos.view.ui.StoreMenus

@Composable
fun StoreMenus(
    onGeneralMenuClicked: () -> Unit,
    onStoreMenuClicked: (menu: StoreMenus) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            items(StoreMenus.values()) {
                StoreMenuItem(stringResource(id = it.title)) {
                    onStoreMenuClicked(it)
                }
            }
        }
        GeneralMenuButtonView(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onMenuClicked = onGeneralMenuClicked
        )
    }
}
