package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.helper.PurchaseProductWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.PurchaseWithSupplier
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasesDao {

    @Query("SELECT * FROM ${Purchase.DB_NAME}")
    fun getPurchases(): Flow<List<PurchaseWithSupplier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchase(data: Purchase)

    @Update
    fun updatePurchase(data: Purchase)

    @Query("SELECT * FROM ${PurchaseProduct.DB_NAME} WHERE ${Purchase.NO} = :purchaseNo")
    fun getPurchasesProduct(purchaseNo: String): Flow<List<PurchaseProductWithProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchaseProducts(data: List<PurchaseProduct>)

    @Update
    fun updatePurchaseProduct(data: PurchaseProduct)
}
