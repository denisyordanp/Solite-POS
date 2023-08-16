package com.socialite.solite_pos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.schema.room.bridge.OrderDetail
import com.socialite.solite_pos.data.schema.room.bridge.OrderPayment
import com.socialite.solite_pos.data.schema.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.schema.room.bridge.OrderPromo
import com.socialite.solite_pos.data.schema.room.helper.OrderData
import com.socialite.solite_pos.data.schema.room.master.Order
import com.socialite.solite_pos.data.schema.room.new_master.Store
import com.socialite.solite_pos.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderProductVariant as NewOrderProductVariant
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.solite_pos.data.schema.room.new_master.Order as NewOrder

@Dao
interface OrdersDao {
    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.STATUS} = :status AND date(${NewOrder.ORDER_DATE}) = date(:date)")
    fun getOrdersByStatus(status: Int, date: String): Flow<List<OrderData>>

    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadOrders(): List<NewOrder>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.STATUS} = :status AND ${Store.ID} = :store AND date(${NewOrder.ORDER_DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOrdersByStatus(
        status: Int,
        from: String,
        until: String,
        store: String
    ): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${Store.ID} = :store AND date(${NewOrder.ORDER_DATE}) BETWEEN date(:from) AND date(:until)")
    fun getAllOrders(
        from: String,
        until: String,
        store: String
    ): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.ID} = :orderId")
    fun getOrderData(orderId: String): Flow<OrderData?>

    @Query("SELECT * FROM '${Order.DB_NAME}'")
    suspend fun getOrders(): List<Order>

    @Query("SELECT * FROM '${NewOrder.DB_NAME}'")
    suspend fun getNewOrders(): List<NewOrder>

    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo LIMIT 1")
    suspend fun getOrderByNo(orderNo: String): Order?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrder(order: NewOrder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(list: List<NewOrder>)

    @Update
    suspend fun updateOrder(order: Order)

    @Update
    suspend fun updateNewOrder(order: NewOrder)

    @Update
    suspend fun updateOrders(order: List<NewOrder>)

    @Query("DELETE FROM '${Order.DB_NAME}'")
    suspend fun deleteAllOldOrders()

    @Query("DELETE FROM '${OrderDetail.DB_NAME}'")
    suspend fun deleteAllOldOrderDetails()

    @Query("DELETE FROM '${OrderPayment.DB_NAME}'")
    suspend fun deleteAllOldOrderPayments()

    @Query("DELETE FROM '${OrderPromo.DB_NAME}'")
    suspend fun deleteAllOldOrderPromos()

    @Query("DELETE FROM '${OrderProductVariant.DB_NAME}'")
    suspend fun deleteAllOldOrderProductVariants()

    @Query("DELETE FROM '${NewOrder.DB_NAME}'")
    suspend fun deleteAllNewOrders()

    @Query("DELETE FROM '${NewOrderDetail.DB_NAME}'")
    suspend fun deleteAllNewOrderDetails()

    @Query("DELETE FROM '${NewOrderPayment.DB_NAME}'")
    suspend fun deleteAllNewOrderPayments()

    @Query("DELETE FROM '${NewOrderPromo.DB_NAME}'")
    suspend fun deleteAllNewOrderPromos()

    @Query("DELETE FROM '${NewOrderProductVariant.DB_NAME}'")
    suspend fun deleteAllNewOrderProductVariants()
}
