package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.schema.room.new_master.Order

fun interface PayOrder {
    suspend operator fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?)
}
