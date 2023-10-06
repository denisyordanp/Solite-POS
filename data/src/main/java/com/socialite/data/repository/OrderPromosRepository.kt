package com.socialite.data.repository

import com.socialite.schema.database.new_bridge.OrderPromo

interface OrderPromosRepository : SyncRepository<OrderPromo> {
    suspend fun getNeedUploadOrderPromos(): List<OrderPromo>
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
}
