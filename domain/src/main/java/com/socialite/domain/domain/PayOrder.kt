package com.socialite.domain.domain

import com.socialite.schema.ui.main.Order
import com.socialite.schema.ui.main.OrderPayment
import com.socialite.schema.ui.main.OrderPromo

fun interface PayOrder {
    suspend operator fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?)
}
