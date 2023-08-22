package com.socialite.domain.domain.impl

import com.socialite.domain.domain.PayOrder
import com.socialite.data.schema.room.new_bridge.OrderPayment
import com.socialite.data.schema.room.new_bridge.OrderPromo
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.repository.OrderPaymentsRepository
import com.socialite.data.repository.OrderPromosRepository
import com.socialite.data.repository.OrdersRepository
import javax.inject.Inject

class PayOrderImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val orderPaymentsRepository: OrderPaymentsRepository,
    private val orderPromosRepository: OrderPromosRepository,
) : PayOrder {
    override suspend fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?) {
        promo?.let {
            orderPromosRepository.insertNewPromoOrder(promo)
        }
        orderPaymentsRepository.insertNewPaymentOrder(payment)
        ordersRepository.updateOrder(order.copy(
            status = Order.DONE
        ))
    }
}
