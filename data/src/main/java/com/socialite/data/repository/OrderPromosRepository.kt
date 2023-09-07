package com.socialite.data.repository

import com.socialite.data.schema.room.new_bridge.OrderPromo

interface OrderPromosRepository : SyncRepository<OrderPromo> {
    suspend fun getNeedUploadOrderPromos(): List<OrderPromo>
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
}
