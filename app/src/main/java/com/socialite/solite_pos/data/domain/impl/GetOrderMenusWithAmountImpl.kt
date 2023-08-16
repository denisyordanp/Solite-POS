package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetOrderMenusWithAmount
import com.socialite.solite_pos.data.schema.helper.MenuOrderAmount
import com.socialite.data.repository.OrdersRepository
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.ui.OrderMenus
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
                val amount = ordersRepository.getOrderList(it.status, parameters.toDataReport()).first().size
                MenuOrderAmount(
                    menu = it,
                    amount = amount
                )
            }
            emit(menus)
        }
    }
}
