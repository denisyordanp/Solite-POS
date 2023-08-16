package com.socialite.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.data.database.AppDatabase
import com.socialite.data.schema.room.bridge.OrderDetail
import com.socialite.data.schema.room.helper.DetailWithVariantOption
import kotlinx.coroutines.flow.Flow
import com.socialite.data.schema.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.data.schema.room.new_master.Order as NewOrder

@Dao
interface OrderDetailsDao {

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0 AND ${NewOrderDetail.DELETED} = 0")
    suspend fun getNeedUploadOrderDetails(): List<NewOrderDetail>

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}'")
    suspend fun getOrderDetails(): List<OrderDetail>

    @Query("SELECT * FROM '${NewOrderDetail.DB_NAME}'")
    suspend fun getNewOrderDetails(): List<NewOrderDetail>

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}' WHERE ${OrderDetail.ID} = :orderDetailId LIMIT 1")
    suspend fun getOrderDetailById(orderDetailId: Long): OrderDetail?

    @Query("SELECT * FROM '${NewOrderDetail.DB_NAME}' WHERE ${NewOrder.ID} = :orderId AND ${NewOrderDetail.DELETED} = 0")
    suspend fun getNewOrderDetailsByOrderId(orderId: String): List<NewOrderDetail>

    @Query("SELECT ${NewOrderDetail.ID} FROM '${NewOrderDetail.DB_NAME}' WHERE ${NewOrderDetail.DELETED} = 1")
    suspend fun getDeletedOrderDetailIds(): List<String>

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrder.ID} = :orderId AND ${NewOrderDetail.DELETED} = 0")
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
