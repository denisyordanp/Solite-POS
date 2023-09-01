package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrdersRepository
import com.socialite.domain.domain.GetOrderMenusWithAmount
import com.socialite.domain.helper.toData
import com.socialite.domain.menu.OrderMenus
import com.socialite.domain.schema.ReportParameter
import com.socialite.domain.schema.MenuOrderAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOrderMenusWithAmountImpl @Inject constructor(
    private val ordersRepository: OrdersRepository
) : GetOrderMenusWithAmount {
    override fun invoke(parameters: ReportParameter): Flow<List<MenuOrderAmount>> {
        return flow {
            val menus = OrderMenus.values().map {
                val amount =
                    ordersRepository.getOrderList(it.status, parameters.toData()).first().size
                MenuOrderAmount(
                    menu = it,
                    amount = amount
                )
            }
            emit(menus)
        }
    }
}
