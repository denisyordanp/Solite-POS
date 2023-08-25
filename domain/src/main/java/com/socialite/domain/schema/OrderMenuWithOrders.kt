package com.socialite.domain.schema.helper

import com.socialite.domain.menu.OrderMenus

data class OrderMenuWithOrders(
    val menu: OrderMenus,
    val orders: List<OrderWithProduct>
)
