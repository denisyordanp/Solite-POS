package com.socialite.domain.schema

import com.socialite.domain.menu.OrderMenus

data class OrderMenuWithOrders(
    val menu: OrderMenus,
    val orders: List<OrderWithProduct>
)
