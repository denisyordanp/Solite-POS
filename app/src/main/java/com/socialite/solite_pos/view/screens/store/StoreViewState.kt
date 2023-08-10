package com.socialite.solite_pos.view.screens.store

import com.socialite.solite_pos.data.source.local.entity.helper.GeneralMenuBadge

data class StoreViewState(
    val badges: List<GeneralMenuBadge>
) {
    companion object {
        fun idle() = StoreViewState(
            badges = emptyList()
        )
    }
}