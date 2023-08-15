package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.schema.room.bridge.OrderPayment
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPayment as NewOrderPayment

@Dao
interface OrderPaymentsDao {

    @Query("SELECT * FROM ${NewOrderPayment.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadOrderPayments(): List<NewOrderPayment>

    @Query("SELECT * FROM '${OrderPayment.DB_NAME}'")
    suspend fun getOrderPayments(): List<OrderPayment>

    @Query("SELECT * FROM '${NewOrderPayment.DB_NAME}'")
    suspend fun getNewOrderPayments(): List<NewOrderPayment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderPayments(list: List<NewOrderPayment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderPayment(payment: NewOrderPayment)

    @Update
    suspend fun updateOrderPayment(orderPayment: OrderPayment)

    @Update
    suspend fun updateNewOrderPayments(orderPayment: List<NewOrderPayment>)
}
