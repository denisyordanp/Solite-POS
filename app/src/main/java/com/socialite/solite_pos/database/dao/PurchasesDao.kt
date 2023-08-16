package com.socialite.solite_pos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.schema.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.schema.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.schema.room.master.Purchase
import com.socialite.solite_pos.data.schema.room.master.PurchaseProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasesDao {

    @Transaction
    @Query("SELECT * FROM ${Purchase.DB_NAME}")
    fun getPurchases(): Flow<List<PurchaseWithSupplier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchase(data: Purchase)

    @Update
    fun updatePurchase(data: Purchase)

    @Transaction
    @Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
    fun getPurchasesProduct(purchaseNo: String): Flow<List<PurchaseProductWithProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchaseProducts(data: List<PurchaseProduct>)

    @Update
    fun updatePurchaseProduct(data: PurchaseProduct)
}
