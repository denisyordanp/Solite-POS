package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository

class PayOrderImpl(
    private val ordersRepository: OrdersRepository
) : PayOrder {
    override suspend fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?) {
        promo?.let {
            ordersRepository.insertNewPromoOrder(promo)
        }
        ordersRepository.insertNewPaymentOrder(payment)
        ordersRepository.updateOrder(order.copy(
            status = Order.DONE
        ))
    }
}
