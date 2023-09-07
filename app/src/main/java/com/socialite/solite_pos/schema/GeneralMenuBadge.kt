package com.socialite.solite_pos.schema

import com.socialite.solite_pos.utils.tools.mapper.toUi
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.domain.schema.GeneralMenuBadge as DomainMenu


data class GeneralMenuBadge(
    val menu: GeneralMenus,
    val badge: Int?
) {
    companion object {
        fun fromDomain(data: DomainMenu): GeneralMenuBadge {
            return GeneralMenuBadge(
                data.menu.toUi(),
                data.badge
            )
        }
    }
}
