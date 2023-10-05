package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.room.helper.OrderData
import com.socialite.domain.domain.GetAllOrderListByReport
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllOrderListByReportImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    private val ordersRepository: OrdersRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetAllOrderListByReport {
    @OptIn(FlowPreview::class)
    override fun invoke(parameters: ReportParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getNewSelectedStore().combine(
                userRepository.getLoggedInUser()
            ) { store, user ->
                Pair(store, user)
            }.flatMapConcat {
                ordersRepository.getOrderList(
                    parameters.start,
                    parameters.end,
                    it.first,
                    it.second?.id?.toLong() ?: 0L
                )
            }
        } else if (parameters.isLoggedInUserOnly()) {
            userRepository.getLoggedInUser().flatMapConcat {
                if (parameters.isAllStoreAndUser()) {
                    ordersRepository.getOrderListAllUserAndStore(
                        from = parameters.start,
                        until = parameters.end
                    )
                } else if (parameters.isAllStore()) {
                    ordersRepository.getOrderListAllStore(
                        from = parameters.start,
                        until = parameters.end,
                        userId = it?.id?.toLong() ?: 0L
                    )
                } else if (parameters.isAllUser()) {
                    ordersRepository.getOrderListAllUser(
                        from = parameters.start,
                        until = parameters.end,
                        store = parameters.storeId
                    )
                } else {
                    ordersRepository.getOrderList(
                        parameters.start,
                        parameters.end,
                        parameters.storeId,
                        it?.id?.toLong() ?: 0L
                    )
                }
            }
        } else {
            if (parameters.isAllStoreAndUser()) {
                ordersRepository.getOrderListAllUserAndStore(
                    from = parameters.start,
                    until = parameters.end
                )
            } else if (parameters.isAllStore()) {
                ordersRepository.getOrderListAllStore(
                    from = parameters.start,
                    until = parameters.end,
                    userId = parameters.userId
                )
            } else if (parameters.isAllUser()) {
                ordersRepository.getOrderListAllUser(
                    from = parameters.start,
                    until = parameters.end,
                    store = parameters.storeId
                )
            } else {
                ordersRepository.getOrderList(
                    parameters.start,
                    parameters.end,
                    parameters.storeId,
                    parameters.userId
                )
            }
        }.flowOn(dispatcher)
    }
}