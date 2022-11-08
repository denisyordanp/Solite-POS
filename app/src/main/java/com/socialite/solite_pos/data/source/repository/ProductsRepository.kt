package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getProductWithCategories(category: Long): Flow<List<ProductWithCategory>>
    fun getAllProductWithCategories(): Flow<List<ProductWithCategory>>
    fun getProductWithCategory(productId: Long): Flow<ProductWithCategory?>
    fun getProductById(productId: Long): Flow<Product>
    suspend fun insertProduct(data: Product): Long
    suspend fun updateProduct(data: Product)
}
