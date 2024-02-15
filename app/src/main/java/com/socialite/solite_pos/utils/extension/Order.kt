package com.socialite.solite_pos.utils.extension

import com.socialite.schema.menu.OrderMenus
import com.socialite.schema.ui.main.Order

fun Order.orderMenuByStatus() = OrderMenus.values().find { it.status == status }