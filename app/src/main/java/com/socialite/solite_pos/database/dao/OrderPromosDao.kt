package com.socialite.solite_pos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.schema.room.bridge.OrderPromo
import com.socialite.solite_pos.database.AppDatabase
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPromo as NewOrderPromo

@Dao
interface OrderPromosDao {

    @Query("SELECT * FROM ${NewOrderPromo.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadOrderPromos(): List<NewOrderPromo>

    @Query("SELECT * FROM '${OrderPromo.DB_NAME}'")
    suspend fun getOrderPromos(): List<OrderPromo>

    @Query("SELECT * FROM '${NewOrderPromo.DB_NAME}'")
    suspend fun getNewOrderPromos(): List<NewOrderPromo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderPromos(list: List<NewOrderPromo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderPromo(promo: NewOrderPromo)

    @Update
    suspend fun updateOrderPromo(orderPromo: OrderPromo)

    @Update
    suspend fun updateOrderPromos(orderPromo: List<NewOrderPromo>)
}
