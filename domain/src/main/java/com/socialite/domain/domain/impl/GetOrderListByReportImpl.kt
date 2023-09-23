package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.room.helper.OrderData
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

class GetOrderListByReportImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    private val ordersRepository: OrdersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrderListByReport {
    @OptIn(FlowPreview::class)
    override fun invoke(status: Int, parameters: ReportParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getNewSelectedStore().combine(
                userRepository.getLoggedInUser()
            ) { store, user ->
                Pair(store, user)
            }.flatMapConcat {
                ordersRepository.getOrderList(
                    status,
                    parameters.start,
                    parameters.end,
                    it.first,
                    it.second?.id?.toLong() ?: 0L
                )
            }
        } else if(parameters.isLoggedInUserOnly()) {
            userRepository.getLoggedInUser().flatMapConcat {
                ordersRepository.getOrderList(
                    status,
                    parameters.start,
                    parameters.end,
                    parameters.storeId,
                    it?.id?.toLongOrDefault(0L) ?: 0L
                )
            }
        } else {
            ordersRepository.getOrderList(
                status,
                parameters.start,
                parameters.end,
                parameters.storeId,
                parameters.userId
            )
        }.flowOn(dispatcher)
    }
}