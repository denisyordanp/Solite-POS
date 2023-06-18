package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct
import kotlinx.coroutines.flow.Flow

interface ProductVariantsRepository : SyncRepository<VariantProduct> {
    fun getVariantOptions(
        productId: String
    ): Flow<List<VariantProductWithOption>?>
    suspend fun getNeedUploadVariantProducts(): List<VariantProduct>
    suspend fun isProductHasVariants(idProduct: String): Boolean
    fun getVariantsProductById(idProduct: String): Flow<List<VariantProduct>>
    fun getVariantProductById(idProduct: String): Flow<VariantProduct?>
    suspend fun insertVariantProduct(data: VariantProduct)
    suspend fun insertVariantProducts(list: List<VariantProduct>)
    suspend fun getProductVariantIds(): List<String>
    suspend fun removeVariantProduct(data: VariantProduct)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldProductVariants()
    suspend fun deleteAllNewProductVariants()
    suspend fun deleteAllDeletedProductVariants()
}
