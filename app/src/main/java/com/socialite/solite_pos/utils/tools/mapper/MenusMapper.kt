package com.socialite.solite_pos.utils.tools.mapper

import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.schema.menu.OrderMenus as DomainOrderMenu
import com.socialite.schema.menu.GeneralMenus as DomainGeneralMenu

fun DomainOrderMenu.toUi(): OrderMenus {
    return when (this) {
        DomainOrderMenu.CURRENT_ORDER -> OrderMenus.CURRENT_ORDER
        DomainOrderMenu.NOT_PAY_YET -> OrderMenus.NOT_PAY_YET
        DomainOrderMenu.CANCELED -> OrderMenus.CANCELED
        DomainOrderMenu.DONE -> OrderMenus.DONE
    }
}

fun DomainGeneralMenu.toUi(): GeneralMenus {
    return when (this) {
        DomainGeneralMenu.NEW_ORDER -> GeneralMenus.NEW_ORDER
        DomainGeneralMenu.ORDERS -> GeneralMenus.ORDERS
        DomainGeneralMenu.STORE -> GeneralMenus.STORE
        DomainGeneralMenu.SETTING -> GeneralMenus.SETTING
    }
}