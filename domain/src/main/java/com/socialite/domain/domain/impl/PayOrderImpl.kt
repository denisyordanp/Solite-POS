package com.socialite.domain.domain.impl

import com.socialite.domain.domain.PayOrder
import com.socialite.data.repository.OrderPaymentsRepository
import com.socialite.data.repository.OrderPromosRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Order
import com.socialite.domain.schema.main.OrderPayment
import com.socialite.domain.schema.main.OrderPromo
import javax.inject.Inject

class PayOrderImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val orderPaymentsRepository: OrderPaymentsRepository,
    private val orderPromosRepository: OrderPromosRepository,
) : PayOrder {
    override suspend fun invoke(order: Order, payment: OrderPayment, promo: OrderPromo?) {
        promo?.toData()?.let {
            orderPromosRepository.insertNewPromoOrder(it)
        }
        orderPaymentsRepository.insertNewPaymentOrder(payment.toData())
        ordersRepository.updateOrder(order.copy(
            status = Order.DONE
        ).toData())
    }
}
