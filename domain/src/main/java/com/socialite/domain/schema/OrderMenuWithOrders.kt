package com.socialite.domain.schema

import com.socialite.schema.menu.OrderMenus
import com.socialite.schema.ui.helper.OrderWithProduct

data class OrderMenuWithOrders(
    val menu: OrderMenus,
    val orders: List<OrderWithProduct>
)
