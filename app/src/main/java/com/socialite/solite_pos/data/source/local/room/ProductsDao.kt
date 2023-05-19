package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product as NewProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct LIMIT 1")
    suspend fun getProduct(idProduct: Long): Product

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct LIMIT 1")
    suspend fun getProductById(idProduct: Long): Product?

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProductAsFlow(idProduct: Long): Flow<Product>

    @Transaction
    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :category")
    fun getProductWithCategories(category: Long): Flow<List<ProductWithCategory>>

    @Transaction
    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :productId")
    fun getProductWithCategory(productId: Long): Flow<ProductWithCategory?>

    @Transaction
    @Query("SELECT * FROM ${Product.DB_NAME}")
    fun getAllProductWithCategories(): Flow<List<ProductWithCategory>>

    @Query("SELECT * FROM '${Product.DB_NAME}'")
    suspend fun getProducts(): List<Product>

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
    fun increaseProductStock(idProduct: Long, amount: Int)

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
    fun decreaseProductStock(idProduct: Long, amount: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(data: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewProduct(data: NewProduct)

    @Update
    suspend fun updateProduct(data: Product)
}
