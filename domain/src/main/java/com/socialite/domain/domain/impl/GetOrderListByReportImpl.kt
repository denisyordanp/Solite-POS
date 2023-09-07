package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.schema.room.helper.OrderData
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetOrderListByReportImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val ordersRepository: OrdersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrderListByReport {
    @OptIn(FlowPreview::class)
    override fun invoke(status: Int, parameters: ReportParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getNewSelectedStore().flatMapConcat {
                ordersRepository.getOrderList(status, parameters.start, parameters.end, it)
            }
        } else {
            ordersRepository.getOrderList(
                status,
                parameters.start,
                parameters.end,
                parameters.storeId
            )
        }.flowOn(dispatcher)
    }
}