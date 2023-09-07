package com.socialite.domain.schema

import com.socialite.domain.menu.OrderMenus

data class MenuOrderAmount(
    val menu: OrderMenus,
    val amount: Int
)
