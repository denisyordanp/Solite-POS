package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

class OrdersRepositoryImpl(
    private val dao: OrdersDao,
    private val settingRepository: SettingRepository
) : OrdersRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrdersRepositoryImpl? = null

        fun getInstance(
            dao: OrdersDao,
            settingRepository: SettingRepository
        ): OrdersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrdersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrdersRepositoryImpl(
                            dao = dao,
                            settingRepository = settingRepository
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOrderList(status: Int, date: String) = dao.getOrdersByStatus(status, date)
    override fun getOrderList(status: Int, date: String, store: Long) =
        dao.getOrdersByStatus(status, date, store)

    @FlowPreview
    override fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getSelectedStore().flatMapConcat {
                dao.getOrdersByStatus(status, parameters.start, parameters.end, it)
            }
        } else {
            dao.getOrdersByStatus(status, parameters.start, parameters.end, parameters.storeId)
        }
    }

    override suspend fun getOrderDetail(orderNo: String): OrderData? = dao.getOrderByNo(orderNo)

    override suspend fun updateOrder(order: Order) = dao.updateOrder(order)

    override suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment {
        val id = dao.insertPaymentOrder(payment)
        payment.id = id
        return payment
    }

    override suspend fun insertNewPaymentOrder(payment: OrderPayment) =
        dao.insertNewPaymentOrder(payment)
}
