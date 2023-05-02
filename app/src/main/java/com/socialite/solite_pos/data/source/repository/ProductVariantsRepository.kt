package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import kotlinx.coroutines.flow.Flow

interface ProductVariantsRepository {

    fun getVariantProduct(
        idProduct: String,
        idVariantOption: String
    ): Flow<VariantProduct?>
    suspend fun getVariantProducts(): List<VariantProduct>
    suspend fun isProductHasVariants(idProduct: String): Boolean
    fun getVariantsProductById(idProduct: String): Flow<List<VariantProduct>>
    fun getVariantProductById(idProduct: String): Flow<VariantProduct?>
    suspend fun insertVariantProduct(data: VariantProduct)
    suspend fun removeVariantProduct(data: VariantProduct)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldProductVariants()
    suspend fun deleteAllNewProductVariants()
}
