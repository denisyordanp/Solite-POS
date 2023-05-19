package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct as NewVariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductVariantsDao {

    @Transaction
    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct ORDER BY ${Variant.ID}")
    fun getVariantProducts(idProduct: Long): Flow<List<VariantProductWithOption>?>

    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
    fun getVariantProduct(idProduct: Long, idVariantOption: Long): Flow<VariantProduct?>

    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProductVariantsById(idProduct: Long): Flow<List<VariantProduct>>

    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProductVariants(idProduct: Long): List<VariantProduct>?

    @Query("SELECT * FROM ${VariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getVariantProductById(idProduct: Long): Flow<VariantProduct?>

    @Query("SELECT * FROM ${VariantProduct.DB_NAME}")
    suspend fun getVariantProducts(): List<VariantProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariantProduct(data: VariantProduct): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVariantProduct(data: NewVariantProduct)

    @Query("DELETE FROM ${VariantProduct.DB_NAME} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
    fun removeVariantProduct(idVariant: Long, idProduct: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProductVariant(data: VariantProduct)
}
