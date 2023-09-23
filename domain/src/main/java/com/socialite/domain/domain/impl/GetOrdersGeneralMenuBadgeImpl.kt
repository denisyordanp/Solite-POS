package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.menu.GeneralMenus
import com.socialite.domain.schema.GeneralMenuBadge
import com.socialite.domain.schema.main.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOrdersGeneralMenuBadgeImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrdersGeneralMenuBadge {

    @OptIn(FlowPreview::class)
    override fun invoke(date: String): Flow<List<GeneralMenuBadge>> {
        return combine(
            settingRepository.getNewSelectedStore(),
            userRepository.getLoggedInUser(),
            flowOf(GeneralMenus.values())
        ) { store, user, menus ->
            Triple(store, user, menus)
        }.flatMapConcat {
            getOrdersCount(date, it.first, it.second?.id?.toLong() ?: 0L).map { orderCount ->
                it.third.map { menu ->
                    val count = if (orderCount == 0) null else orderCount
                    val badge = if (menu == GeneralMenus.ORDERS) count else null
                    GeneralMenuBadge(menu, badge)
                }
            }
        }.flowOn(dispatcher)
    }

    private fun getOrdersCount(date: String, storeId: String?, userId: Long): Flow<Int> {
        return if (storeId != null) {
            val onProcess = ordersRepository.getOrderList(
                status = Order.ON_PROCESS,
                date = date,
                storeId = storeId,
                userId = userId
            )
            val needPays = ordersRepository.getOrderList(
                status = Order.NEED_PAY,
                date = date,
                storeId = storeId,
                userId = userId
            )
            return onProcess.combine(needPays) { process, needPay ->
                process.size + needPay.size
            }
        } else {
            flowOf(0)
        }.flowOn(dispatcher)
    }
}
