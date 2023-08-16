package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.schema.room.new_bridge.OrderProductVariant

interface OrderProductVariantsRepository : SyncRepository<OrderProductVariant> {
    suspend fun getNeedUploadOrderProductVariants(): List<OrderProductVariant>
    suspend fun migrateToUUID()
    suspend fun insertOrderProductVariants(list: List<OrderProductVariant>)
    suspend fun insertOrderProductVariant(data: OrderProductVariant)
    suspend fun getDeletedOrderProductVariantIds(): List<String>
    suspend fun deleteAllDeletedOrderProductVariants()
}
