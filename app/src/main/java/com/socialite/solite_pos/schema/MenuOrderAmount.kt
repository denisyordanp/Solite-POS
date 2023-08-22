package com.socialite.solite_pos.schema

import com.socialite.solite_pos.utils.tools.mapper.toUi
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.domain.schema.helper.MenuOrderAmount as DomainMenuAmount

data class MenuOrderAmount(
    val menu: OrderMenus,
    val amount: Int
) {
    companion object {
        fun fromDomain(data: DomainMenuAmount): MenuOrderAmount {
            return MenuOrderAmount(
                data.menu.toUi(),
                data.amount
            )
        }
    }
}
