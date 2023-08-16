package com.socialite.data.repository

import com.socialite.data.schema.room.helper.VariantProductWithOption
import com.socialite.data.schema.room.new_bridge.VariantProduct
import kotlinx.coroutines.flow.Flow

interface ProductVariantsRepository : SyncRepository<VariantProduct> {
    fun getVariantOptions(
        productId: String
    ): Flow<List<VariantProductWithOption>?>
    fun getAllVariantOptions(): Flow<List<VariantProductWithOption>>
    suspend fun getNeedUploadVariantProducts(): List<VariantProduct>
    suspend fun isProductHasVariants(idProduct: String): Boolean
    fun getVariantsProductById(idProduct: String): Flow<List<VariantProduct>>
    suspend fun insertVariantProduct(data: VariantProduct)
    suspend fun getProductVariantIds(): List<String>
    suspend fun removeVariantProduct(data: VariantProduct)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldProductVariants()
    suspend fun deleteAllNewProductVariants()
    suspend fun deleteAllDeletedProductVariants()
}
