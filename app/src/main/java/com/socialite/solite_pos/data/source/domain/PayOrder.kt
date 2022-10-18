package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Order

interface PayOrder {
    suspend operator fun invoke(order: Order, payment: OrderPayment)
}
