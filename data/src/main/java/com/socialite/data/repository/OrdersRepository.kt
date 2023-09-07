package com.socialite.data.repository

import com.socialite.data.schema.room.helper.OrderData
import com.socialite.data.schema.room.new_master.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository : SyncRepository<Order> {

    fun getOrderList(
        status: Int,
        storeId: String,
        date: String
    ): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(
        status: Int,
        from: String,
        until: String,
        store: String
    ): Flow<List<OrderData>>

    fun getOrderList(
        from: String,
        until: String,
        store: String
    ): Flow<List<OrderData>>
    fun getOrderDataAsFlow(orderId: String): Flow<OrderData?>
    suspend fun updateOrder(order: Order)
    suspend fun insertOrder(order: Order)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
}
