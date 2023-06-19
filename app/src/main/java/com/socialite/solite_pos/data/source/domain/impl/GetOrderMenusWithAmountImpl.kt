package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.source.local.entity.helper.MenuOrderAmount
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import com.socialite.solite_pos.view.ui.OrderMenus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetOrderMenusWithAmountImpl(
    private val ordersRepository: OrdersRepository
) : GetOrderMenusWithAmount {
    override fun invoke(parameters: ReportsParameter): Flow<List<MenuOrderAmount>> {
        return flow {
            val menus = OrderMenus.values().map {
                val amount = ordersRepository.getOrderList(it.status, parameters).first().size
                MenuOrderAmount(
                    menu = it,
                    amount = amount
                )
            }
            emit(menus)
        }
    }
}