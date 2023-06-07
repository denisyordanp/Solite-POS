package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant as NewOrderProductVariant

@Dao
interface OrderProductVariantsDao {

    @Query("SELECT * FROM ${NewOrderProductVariant.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0 AND ${NewOrderDetail.DELETED} = 0")
    suspend fun getNeedOrderProductVariants(): List<NewOrderProductVariant>

    @Query("SELECT * FROM '${OrderProductVariant.DB_NAME}'")
    suspend fun getOrderProductVariants(): List<OrderProductVariant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderProductVariant(orderProductVariant: NewOrderProductVariant)

    @Query("SELECT ${NewOrderProductVariant.ID} FROM '${NewOrderProductVariant.DB_NAME}' WHERE ${NewOrderProductVariant.DELETED} = 1")
    suspend fun getDeletedOrderProductVariantIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProductVariants(list: List<NewOrderProductVariant>)

    @Update
    suspend fun updateOrderProductVariant(orderProductVariant: OrderProductVariant)

    @Query("UPDATE '${NewOrderProductVariant.DB_NAME}' SET ${NewOrderProductVariant.DELETED} = 1 WHERE ${NewOrderDetail.ID} = :orderDetailId")
    suspend fun updateOrderProductVariantsByDetailId(orderDetailId: String)

    @Query("DELETE FROM '${NewOrderProductVariant.DB_NAME}' WHERE ${NewOrderProductVariant.DELETED} = 1")
    suspend fun deleteAllDeletedOrderProductVariants()
}
