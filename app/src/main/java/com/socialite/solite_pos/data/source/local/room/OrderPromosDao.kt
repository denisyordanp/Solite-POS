package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo as NewOrderPromo

@Dao
interface OrderPromosDao {

    @Query("SELECT * FROM ${NewOrderPromo.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadOrderPromos(): List<NewOrderPromo>

    @Query("SELECT * FROM '${OrderPromo.DB_NAME}'")
    suspend fun getOrderPromos(): List<OrderPromo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderPromos(list: List<NewOrderPromo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderPromo(promo: NewOrderPromo)

    @Update
    suspend fun updateOrderPromo(orderPromo: OrderPromo)
}
