package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetOrdersGeneralMenuBadge
import com.socialite.solite_pos.data.source.local.entity.helper.GeneralMenuBadge
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.repository.OrdersRepository
import com.socialite.solite_pos.view.ui.GeneralMenus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetOrdersGeneralMenuBadgeImpl @Inject constructor(
    private val ordersRepository: OrdersRepository
) : GetOrdersGeneralMenuBadge {

    override fun invoke(date: String): Flow<List<GeneralMenuBadge>> {
        return flowOf(GeneralMenus.values())
            .combine(getOrdersCount(date = date)) { menus, orders ->
                menus.map {
                    val count = if (orders == 0) null else orders
                    val badge = if (it == GeneralMenus.ORDERS) count else null
                    GeneralMenuBadge(it, badge)
                }
            }
    }

    private fun getOrdersCount(date: String): Flow<Int> {
        val onProcess = ordersRepository.getOrderList(Order.ON_PROCESS, date)
        val needPays = ordersRepository.getOrderList(Order.NEED_PAY, date)
        return onProcess.combine(needPays) { process, needPay ->
            process.size + needPay.size
        }
    }
}
