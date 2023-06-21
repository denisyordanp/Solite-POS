package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository : SyncRepository<Product> {
    fun getActiveProductsWithCategory(): Flow<List<ProductWithCategory>>
    fun getAllProductsWithCategory(): Flow<List<ProductWithCategory>>
    fun getProductWithCategory(productId: String): Flow<ProductWithCategory?>
    fun getProductById(productId: String): Flow<Product>
    suspend fun getNeedUploadProducts(): List<Product>
    suspend fun insertProduct(data: Product)
    suspend fun updateProduct(data: Product)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldProducts()
    suspend fun deleteAllNewProducts()
}
