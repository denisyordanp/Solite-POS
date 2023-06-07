package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo

interface OrderPromosRepository {
    suspend fun getNeedUploadOrderPromos(): List<OrderPromo>
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
    suspend fun insertOrderPromos(list: List<OrderPromo>)
}
