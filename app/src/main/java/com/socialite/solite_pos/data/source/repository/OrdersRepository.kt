package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow

interface OrdersRepository : SyncRepository<Order> {

    fun getOrderList(status: Int, date: String): Flow<List<OrderData>>
    fun getOrderList(status: Int, date: String, store: String): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>>
    fun getAllOrderList(parameters: ReportsParameter): Flow<List<OrderData>>
    fun getOrderDataAsFlow(orderId: String): Flow<OrderData?>
    suspend fun getOrderData(orderId: String): OrderData?
    suspend fun updateOrder(order: Order)
    suspend fun insertOrder(order: Order)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
}
