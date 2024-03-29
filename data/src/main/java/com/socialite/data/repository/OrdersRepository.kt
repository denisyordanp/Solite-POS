package com.socialite.data.repository

import com.socialite.data.schema.room.helper.OrderData
import com.socialite.data.schema.room.new_master.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository : SyncRepository<Order> {

    fun getOrderList(
        status: Int,
        date: String,
        storeId: String,
        userId: Long
    ): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(
        status: Int,
        from: String,
        until: String,
        store: String,
        userId: Long
    ): Flow<List<OrderData>>

    fun getOrderAllStoreList(
        status: Int,
        from: String,
        until: String,
        userId: Long
    ): Flow<List<OrderData>>

    fun getOrderAllUserList(
        status: Int,
        from: String,
        until: String,
        store: String,
    ): Flow<List<OrderData>>

    fun getOrderAllUserAndStoreList(
        status: Int,
        from: String,
        until: String,
    ): Flow<List<OrderData>>

    fun getOrderList(
        from: String,
        until: String,
        store: String,
        userId: Long
    ): Flow<List<OrderData>>

    fun getOrderListAllStore(
        from: String,
        until: String,
        userId: Long
    ): Flow<List<OrderData>>

    fun getOrderListAllUser(
        from: String,
        until: String,
        store: String,
    ): Flow<List<OrderData>>

    fun getOrderListAllUserAndStore(
        from: String,
        until: String,
    ): Flow<List<OrderData>>

    fun getOrderDataAsFlow(orderId: String): Flow<OrderData?>
    suspend fun updateOrder(order: Order)
    suspend fun insertOrder(order: Order)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
}
