package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import kotlinx.coroutines.flow.Flow
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct as NewVariantProduct

@Dao
interface ProductVariantsDao {

    @Transaction
    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct ORDER BY ${Variant.ID}")
    fun getVariantProducts(idProduct: String): Flow<List<VariantProductWithOption>?>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${VariantOption.ID} = :idVariantOption")
    fun getVariantProduct(idProduct: String, idVariantOption: String): Flow<NewVariantProduct?>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${NewVariantProduct.DELETED} = 0")
    fun getProductVariantsById(idProduct: String): Flow<List<NewVariantProduct>>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProductVariants(idProduct: String): List<NewVariantProduct>?

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getVariantProductById(idProduct: String): Flow<NewVariantProduct?>

    @Query("SELECT * FROM ${VariantProduct.DB_NAME}")
    suspend fun getVariantProducts(): List<VariantProduct>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME}")
    suspend fun getNewVariantProducts(): List<NewVariantProduct>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0 AND ${NewVariantProduct.DELETED} = 0")
    suspend fun getNeedUploadVariantProducts(): List<NewVariantProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantProduct(data: VariantProduct): Long

    @Query("SELECT ${NewVariantProduct.ID} FROM '${NewVariantProduct.DB_NAME}' WHERE ${NewVariantProduct.DELETED} = 1")
    suspend fun getProductVariantIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantProducts(list: List<NewVariantProduct>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVariantProduct(data: NewVariantProduct)

    @Query("DELETE FROM ${NewVariantProduct.DB_NAME} WHERE ${VariantOption.ID} = :idVariant AND ${Product.ID} = :idProduct")
    fun removeVariantProduct(idVariant: String, idProduct: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProductVariant(data: VariantProduct)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNewVariantProduct(data: NewVariantProduct)

    @Update
    suspend fun updateVariantProducts(data: List<NewVariantProduct>)

    @Query("DELETE FROM ${VariantProduct.DB_NAME}")
    suspend fun deleteAllOldProductVariants()

    @Query("DELETE FROM ${NewVariantProduct.DB_NAME}")
    suspend fun deleteAllNewProductVariants()

    @Query("DELETE FROM ${NewVariantProduct.DB_NAME} WHERE ${NewVariantProduct.DELETED} = 1")
    suspend fun deleteAllDeletedProductVariants()
}
