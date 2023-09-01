package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.domain.UpdateOrder
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Order
import javax.inject.Inject

class UpdateOrderImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
) : UpdateOrder {
    override suspend fun invoke(order: Order) {
        ordersRepository.updateOrder(order.toData())
    }
}