package com.socialite.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.data.database.AppDatabase
import com.socialite.data.schema.room.helper.ProductWithCategory
import com.socialite.data.schema.room.master.Product
import com.socialite.data.schema.room.new_master.Product as NewProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM ${NewProduct.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadProducts(): List<NewProduct>

    @Query("SELECT * FROM ${Product.DB_NAME}")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM ${NewProduct.DB_NAME}")
    suspend fun getNewProducts(): List<NewProduct>

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct LIMIT 1")
    suspend fun getProductById(idProduct: Long): Product?

    @Query("SELECT * FROM ${NewProduct.DB_NAME} WHERE ${NewProduct.ID} = :idProduct")
    fun getProductAsFlow(idProduct: String): Flow<NewProduct>

    @Transaction
    @Query("SELECT * FROM ${NewProduct.DB_NAME} WHERE ${NewProduct.ID} = :productId")
    fun getProductWithCategory(productId: String): Flow<ProductWithCategory?>

    @Transaction
    @Query("SELECT * FROM ${NewProduct.DB_NAME} WHERE ${NewProduct.STATUS} = 1")
    fun getActiveProductsWithCategory(): Flow<List<ProductWithCategory>>

    @Transaction
    @Query("SELECT * FROM ${NewProduct.DB_NAME}")
    fun getAllProductsWithCategory(): Flow<List<ProductWithCategory>>

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
    fun increaseProductStock(idProduct: Long, amount: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewProduct(data: NewProduct)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(list: List<NewProduct>)

    @Update
    suspend fun updateProduct(data: Product)

    @Update
    suspend fun updateNewProduct(data: NewProduct)

    @Update
    suspend fun updateProducts(data: List<NewProduct>)

    @Query("DELETE FROM '${Product.DB_NAME}'")
    suspend fun deleteAllOldProducts()

    @Query("DELETE FROM '${NewProduct.DB_NAME}'")
    suspend fun deleteAllNewProducts()
}
