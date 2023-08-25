package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrdersRepository
import com.socialite.data.schema.room.new_master.Order
import com.socialite.domain.domain.UpdateOrder
import javax.inject.Inject

class UpdateOrderImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
) : UpdateOrder {
    override suspend fun invoke(order: Order) {
        ordersRepository.updateOrder(order)
    }
}