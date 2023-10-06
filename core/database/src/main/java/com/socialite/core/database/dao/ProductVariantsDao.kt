package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.schema.database.bridge.VariantProduct
import com.socialite.schema.database.helper.VariantProductWithOption
import com.socialite.schema.database.new_master.Product
import com.socialite.schema.database.new_master.Variant
import kotlinx.coroutines.flow.Flow
import com.socialite.schema.database.new_bridge.VariantProduct as NewVariantProduct

@Dao
interface ProductVariantsDao {

    @Transaction
    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct ORDER BY ${Variant.ID}")
    fun getVariantProducts(idProduct: String): Flow<List<VariantProductWithOption>?>

    @Transaction
    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${NewVariantProduct.DELETED} = 0 ORDER BY ${Variant.ID}")
    fun getAllVariantProducts(): Flow<List<VariantProductWithOption>>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${NewVariantProduct.DELETED} = 0")
    fun getProductVariantsById(idProduct: String): Flow<List<NewVariantProduct>>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${Product.ID} = :idProduct AND ${NewVariantProduct.DELETED} = 0")
    fun getProductVariants(idProduct: String): List<NewVariantProduct>?

    @Query("SELECT * FROM ${VariantProduct.DB_NAME}")
    suspend fun getVariantProducts(): List<VariantProduct>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME}")
    suspend fun getNewVariantProducts(): List<NewVariantProduct>

    @Query("SELECT * FROM ${NewVariantProduct.DB_NAME} WHERE ${NewVariantProduct.UPLOAD} = 0 AND ${NewVariantProduct.DELETED} = 0")
    suspend fun getNeedUploadVariantProducts(): List<NewVariantProduct>

    @Query("SELECT ${NewVariantProduct.ID} FROM '${NewVariantProduct.DB_NAME}' WHERE ${NewVariantProduct.DELETED} = 1")
    suspend fun getProductVariantIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantProducts(list: List<NewVariantProduct>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVariantProduct(data: NewVariantProduct)

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
