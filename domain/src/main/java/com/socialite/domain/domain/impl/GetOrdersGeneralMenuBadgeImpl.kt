package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.GetOrdersGeneralMenuBadge
import com.socialite.domain.menu.GeneralMenus
import com.socialite.domain.schema.GeneralMenuBadge
import com.socialite.domain.schema.main.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetOrdersGeneralMenuBadgeImpl @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val settingRepository: SettingRepository,
    private val storeRepository: StoreRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOrdersGeneralMenuBadge {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun invoke(date: String): Flow<List<GeneralMenuBadge>> {
        return settingRepository.getNewSelectedStore()
            .mapLatest {
                storeRepository.getStore(it)
            }.flatMapConcat {
                getOrdersCount(date, it?.id)
            }.combine(
                flowOf(GeneralMenus.values())
            ) { orderCount, menus ->
                menus.map {
                    val count = if (orderCount == 0) null else orderCount
                    val badge = if (it == GeneralMenus.ORDERS) count else null
                    GeneralMenuBadge(it, badge)
                }
            }.flowOn(dispatcher)
    }

    private fun getOrdersCount(date: String, storeId: String?): Flow<Int> {
        return if (storeId != null) {
            val onProcess = ordersRepository.getOrderList(Order.ON_PROCESS, storeId, date)
            val needPays = ordersRepository.getOrderList(Order.NEED_PAY, storeId, date)
            return onProcess.combine(needPays) { process, needPay ->
                process.size + needPay.size
            }
        } else {
            flowOf(0)
        }.flowOn(dispatcher)
    }
}
