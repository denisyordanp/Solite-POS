package com.socialite.solite_pos.view.screens.store

import com.socialite.common.menus.StoreMenus
import com.socialite.solite_pos.schema.GeneralMenuBadge
import com.socialite.solite_pos.schema.Store
import com.socialite.solite_pos.schema.User


data class StoreViewState(
    val badges: List<GeneralMenuBadge>,
    val menus: List<StoreMenus>,
    val user: User?,
    val store: Store?
) {
    companion object {
        fun idle() = StoreViewState(
            badges = emptyList(),
            menus = emptyList(),
            user = null,
            store = null
        )
    }
}
