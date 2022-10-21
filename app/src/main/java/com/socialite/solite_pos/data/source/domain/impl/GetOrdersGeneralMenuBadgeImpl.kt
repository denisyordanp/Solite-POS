package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import kotlinx.coroutines.flow.zip

class GetOrdersGeneralMenuBadgeImpl(
    private val ordersRepository: OrdersRepository
) : GetOrdersGeneralMenuBadge {
    override fun invoke(date: String) = ordersRepository.getOrderList(Order.ON_PROCESS, date)
        .zip(ordersRepository.getOrderList(Order.NEED_PAY, date)) { process, needPay ->
            process.size + needPay.size
        }
}
