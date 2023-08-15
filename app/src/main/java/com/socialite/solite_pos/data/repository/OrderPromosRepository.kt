package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo

interface OrderPromosRepository : SyncRepository<OrderPromo> {
    suspend fun getNeedUploadOrderPromos(): List<OrderPromo>
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
}
