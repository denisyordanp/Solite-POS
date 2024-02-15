package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.schema.database.bridge.OrderDetail
import com.socialite.schema.database.helper.DetailWithVariantOption
import com.socialite.schema.database.new_master.Order
import kotlinx.coroutines.flow.Flow
import com.socialite.schema.database.new_bridge.OrderDetail as NewOrderDetail

@Dao
interface OrderDetailsDao {

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrderDetail.UPLOAD} = 0 AND ${NewOrderDetail.DELETED} = 0")
    suspend fun getNeedUploadOrderDetails(): List<NewOrderDetail>

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}'")
    suspend fun getOrderDetails(): List<OrderDetail>

    @Query("SELECT * FROM '${NewOrderDetail.DB_NAME}'")
    suspend fun getNewOrderDetails(): List<NewOrderDetail>

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}' WHERE ${OrderDetail.ID} = :orderDetailId LIMIT 1")
    suspend fun getOrderDetailById(orderDetailId: Long): OrderDetail?

    @Query("SELECT * FROM '${NewOrderDetail.DB_NAME}' WHERE ${Order.ID} = :orderId AND ${NewOrderDetail.DELETED} = 0")
    suspend fun getNewOrderDetailsByOrderId(orderId: String): List<NewOrderDetail>

    @Query("SELECT ${NewOrderDetail.ID} FROM '${NewOrderDetail.DB_NAME}' WHERE ${NewOrderDetail.DELETED} = 1")
    suspend fun getDeletedOrderDetailIds(): List<String>

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${Order.ID} = :orderId AND ${NewOrderDetail.DELETED} = 0")
    fun getOrderDetailByIdOrder(orderId: String): Flow<List<NewOrderDetail>>

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrderDetail.DELETED} = 0")
    fun getOrderDetailsFlow(): Flow<List<NewOrderDetail>>

    @Transaction
    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrderDetail.ID} = :idDetail")
    suspend fun getOrderDetailWithVariants(idDetail: String): DetailWithVariantOption

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderDetail(detail: NewOrderDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderDetails(list: List<NewOrderDetail>)

    @Update
    suspend fun updateOrderDetail(orderDetail: OrderDetail)

    @Update
    suspend fun updateNewOrderDetail(orderDetail: NewOrderDetail)

    @Update
    suspend fun updateOrderDetails(orderDetail: List<NewOrderDetail>)

    @Query("DELETE FROM '${NewOrderDetail.DB_NAME}' WHERE ${NewOrderDetail.DELETED} = 1")
    suspend fun deleteAllDeletedOrderDetails()
}
