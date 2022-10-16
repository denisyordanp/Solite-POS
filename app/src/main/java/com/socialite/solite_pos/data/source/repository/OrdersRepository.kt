package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    fun getOrderList(status: Int, date: String): Flow<List<OrderData>>
    suspend fun getOrderDetail(orderNo: String): OrderData?
    suspend fun updateOrder(order: Order)
    suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment
}
