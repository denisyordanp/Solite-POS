package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getProductWithCategories(category: String): Flow<List<ProductWithCategory>>
    fun getAllProductWithCategories(): Flow<List<ProductWithCategory>>
    fun getProductWithCategory(productId: String): Flow<ProductWithCategory?>
    fun getProductById(productId: String): Flow<Product>
    suspend fun insertProduct(data: Product)
    suspend fun updateProduct(data: Product)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldProducts()
    suspend fun deleteAllNewProducts()
}
