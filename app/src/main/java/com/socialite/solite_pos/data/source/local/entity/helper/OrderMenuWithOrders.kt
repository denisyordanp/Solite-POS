package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.view.ui.OrderMenus

data class OrderMenuWithOrders(
    val menu: OrderMenus,
    val orders: List<OrderWithProduct>
) {
    fun getBadges() = when (menu) {
        OrderMenus.CURRENT_ORDER, OrderMenus.NOT_PAY_YET -> if (orders.isEmpty()) null else orders.size
        else -> null
    }
}
