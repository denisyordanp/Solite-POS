package com.socialite.domain.domain

import com.socialite.domain.schema.main.Order
import com.socialite.domain.schema.main.OrderPayment
import com.socialite.domain.schema.main.OrderPromo

fun interface PayOrder {
    suspend operator fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?)
}
