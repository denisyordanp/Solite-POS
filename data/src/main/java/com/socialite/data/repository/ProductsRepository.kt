package com.socialite.data.repository

import com.socialite.data.schema.room.helper.ProductWithCategory
import com.socialite.data.schema.room.new_master.Product
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
