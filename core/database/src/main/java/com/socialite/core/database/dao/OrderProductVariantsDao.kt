package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.schema.database.bridge.OrderProductVariant
import com.socialite.schema.database.new_bridge.OrderDetail
import com.socialite.schema.database.new_bridge.OrderProductVariant as NewOrderProductVariant

@Dao
interface OrderProductVariantsDao {

    @Query("SELECT * FROM ${NewOrderProductVariant.DB_NAME} WHERE ${NewOrderProductVariant.UPLOAD} = 0 AND ${OrderDetail.DELETED} = 0")
    suspend fun getNeedOrderProductVariants(): List<NewOrderProductVariant>

    @Query("SELECT * FROM '${OrderProductVariant.DB_NAME}'")
    suspend fun getOrderProductVariants(): List<OrderProductVariant>

    @Query("SELECT * FROM '${NewOrderProductVariant.DB_NAME}'")
    suspend fun getNewOrderProductVariants(): List<NewOrderProductVariant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderProductVariant(orderProductVariant: NewOrderProductVariant)

    @Query("SELECT ${NewOrderProductVariant.ID} FROM '${NewOrderProductVariant.DB_NAME}' WHERE ${NewOrderProductVariant.DELETED} = 1")
    suspend fun getDeletedOrderProductVariantIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProductVariants(list: List<NewOrderProductVariant>)

    @Update
    suspend fun updateOrderProductVariant(orderProductVariant: OrderProductVariant)

    @Update
    suspend fun updateOrderProductVariants(orderProductVariant: List<NewOrderProductVariant>)

    @Query("UPDATE '${NewOrderProductVariant.DB_NAME}' SET ${NewOrderProductVariant.DELETED} = 1 WHERE ${OrderDetail.ID} = :orderDetailId")
    suspend fun updateOrderProductVariantsByDetailId(orderDetailId: String)

    @Query("DELETE FROM '${NewOrderProductVariant.DB_NAME}' WHERE ${NewOrderProductVariant.DELETED} = 1")
    suspend fun deleteAllDeletedOrderProductVariants()
}
