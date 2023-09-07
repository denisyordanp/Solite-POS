package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.menu.GeneralMenus
import com.socialite.domain.schema.GeneralMenuBadge
import com.socialite.domain.schema.main.Order
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
