package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.extensions.toLongDefault
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.schema.database.helper.OrderData
import com.socialite.domain.domain.GetOrderListByReport
import com.socialite.schema.ui.helper.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
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
                    status = status,
                    from = parameters.start,
                    until = parameters.end,
                    store = it.first,
                    userId = it.second?.id?.toLong() ?: 0L
                )
            }
        } else if (parameters.isLoggedInUserOnly()) {
            userRepository.getLoggedInUser().flatMapConcat {
                if (parameters.isAllStoreAndUser()) {
                    ordersRepository.getOrderAllUserAndStoreList(
                        status = status,
                        from = parameters.start,
                        until = parameters.end
                    )
                } else if (parameters.isAllStore()) {
                    ordersRepository.getOrderAllStoreList(
                        status = status,
                        from = parameters.start,
                        until = parameters.end,
                        userId = it?.id?.toLongDefault(0L) ?: 0L
                    )
                } else if (parameters.isAllUser()) {
                    ordersRepository.getOrderAllUserList(
                        status = status,
                        from = parameters.start,
                        until = parameters.end,
                        store = parameters.storeId
                    )
                } else {
                    ordersRepository.getOrderList(
                        status = status,
                        from = parameters.start,
                        until = parameters.end,
                        store = parameters.storeId,
                        userId = it?.id?.toLongDefault(0L) ?: 0L
                    )
                }
            }
        } else {
            if (parameters.isAllStoreAndUser()) {
                ordersRepository.getOrderAllUserAndStoreList(
                    status = status,
                    from = parameters.start,
                    until = parameters.end
                )
            } else if (parameters.isAllStore()) {
                ordersRepository.getOrderAllStoreList(
                    status = status,
                    from = parameters.start,
                    until = parameters.end,
                    userId = parameters.userId
                )
            } else if (parameters.isAllUser()) {
                ordersRepository.getOrderAllUserList(
                    status = status,
                    from = parameters.start,
                    until = parameters.end,
                    store = parameters.storeId
                )
            } else {
                ordersRepository.getOrderList(
                    status = status,
                    from = parameters.start,
                    until = parameters.end,
                    userId = parameters.userId,
                    store = parameters.storeId
                )
            }
        }.flowOn(dispatcher)
    }
}