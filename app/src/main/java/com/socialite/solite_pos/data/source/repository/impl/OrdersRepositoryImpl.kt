package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.repository.OrdersRepository

class OrdersRepositoryImpl(
    private val dao: OrdersDao
) : OrdersRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrdersRepositoryImpl? = null

        fun getInstance(
            dao: OrdersDao
        ): OrdersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrdersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrdersRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOrderList(status: Int, date: String) = dao.getOrdersByStatus(status, date)

    override suspend fun updateOrder(order: Order) {
        dao.updateOrder(order)
    }

    override suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment {
        val id = dao.insertPaymentOrder(payment)
        payment.id = id
        return payment
    }
}
