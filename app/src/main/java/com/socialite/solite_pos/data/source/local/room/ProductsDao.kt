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
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct")
    fun getProduct(idProduct: Long): Product

    @Transaction
    @Query("SELECT * FROM ${Product.DB_NAME} WHERE ${Category.ID} = :category")
    fun getProductWithCategories(category: Long): Flow<List<ProductWithCategory>>

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) + :amount) WHERE ${Product.ID} = :idProduct")
    fun increaseProductStock(idProduct: Long, amount: Int)

    @Query("UPDATE ${Product.DB_NAME} SET ${Product.STOCK} = ((SELECT ${Product.STOCK} FROM ${Product.DB_NAME} WHERE ${Product.ID} = :idProduct) - :amount) WHERE ${Product.ID} = :idProduct")
    fun decreaseProductStock(idProduct: Long, amount: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(data: Product): Long

    @Update
    fun updateProduct(data: Product)
}
