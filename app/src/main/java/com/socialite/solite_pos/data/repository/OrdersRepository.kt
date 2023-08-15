package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.schema.room.helper.OrderData
import com.socialite.solite_pos.data.schema.room.new_master.Order
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

interface OrdersRepository : SyncRepository<Order> {

    fun getOrderList(status: Int, date: String): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(status: Int, parameters: ReportParameter): Flow<List<OrderData>>
    fun getAllOrderList(parameters: ReportParameter): Flow<List<OrderData>>
    fun getOrderDataAsFlow(orderId: String): Flow<OrderData?>
    suspend fun updateOrder(order: Order)
    suspend fun insertOrder(order: Order)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
}
