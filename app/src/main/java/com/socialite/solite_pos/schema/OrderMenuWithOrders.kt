package com.socialite.solite_pos.schema

import com.socialite.domain.schema.OrderWithProduct
import com.socialite.solite_pos.utils.tools.mapper.toUi
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.domain.schema.OrderMenuWithOrders as DomainOrders

data class OrderMenuWithOrders(
    val menu: OrderMenus,
    val orders: List<OrderWithProduct>
) {
    fun getBadges() = when (menu) {
        OrderMenus.CURRENT_ORDER, OrderMenus.NOT_PAY_YET -> if (orders.isEmpty()) null else orders.size
        else -> null
    }

    companion object {
        fun fromDomain(data: DomainOrders): OrderMenuWithOrders {
            return OrderMenuWithOrders(
                data.menu.toUi(),
                data.orders
            )
        }
    }
}
