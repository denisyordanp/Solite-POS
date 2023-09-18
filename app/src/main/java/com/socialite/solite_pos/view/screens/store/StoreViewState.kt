package com.socialite.solite_pos.view.screens.store

import com.socialite.common.menus.StoreMenus
import com.socialite.solite_pos.schema.GeneralMenuBadge


data class StoreViewState(
    val badges: List<GeneralMenuBadge>,
    val menus: List<StoreMenus>
) {
    companion object {
        fun idle() = StoreViewState(
            badges = emptyList(),
            menus = emptyList()
        )
    }
}
