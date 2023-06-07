package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant

interface OrderProductVariantsRepository {
    suspend fun getNeedUploadOrderProductVariants(): List<OrderProductVariant>
    suspend fun migrateToUUID()
    suspend fun insertOrderProductVariants(list: List<OrderProductVariant>)
    suspend fun insertOrderProductVariant(data: OrderProductVariant)
    suspend fun getDeletedOrderProductVariantIds(): List<String>
    suspend fun deleteAllDeletedOrderProductVariants()
}
