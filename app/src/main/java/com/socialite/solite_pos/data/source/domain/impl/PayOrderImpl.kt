package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository

class PayOrderImpl(
    private val ordersRepository: OrdersRepository,
    private val orderPaymentsRepository: OrderPaymentsRepository
) : PayOrder {
    override suspend fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?) {
        promo?.let {
            ordersRepository.insertNewPromoOrder(promo)
        }
        orderPaymentsRepository.insertNewPaymentOrder(payment)
        ordersRepository.updateOrder(order.copy(
            status = Order.DONE
        ))
    }
}
