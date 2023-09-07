package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.domain.domain.GetOrderMenusWithAmount
import com.socialite.domain.menu.OrderMenus
import com.socialite.domain.schema.MenuOrderAmount
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetOrderMenusWithAmountImpl @Inject constructor(
    private val getOrderListByReport: GetOrderListByReport,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrderMenusWithAmount {
    override fun invoke(parameters: ReportParameter): Flow<List<MenuOrderAmount>> {
        return flow {
            val menus = OrderMenus.values().map {
                val amount = getOrderListByReport(it.status, parameters).first().size
                MenuOrderAmount(
                    menu = it,
                    amount = amount
                )
            }
            emit(menus)
        }.flowOn(dispatcher)
    }
}
