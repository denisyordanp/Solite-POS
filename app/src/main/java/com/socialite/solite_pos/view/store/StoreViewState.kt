package com.socialite.solite_pos.view.store

import com.socialite.solite_pos.data.source.local.entity.helper.MenuBadge

data class StoreViewState(
    val badges: List<MenuBadge>
) {
    companion object {
        fun idle() = StoreViewState(
            badges = emptyList()
        )
    }
}
