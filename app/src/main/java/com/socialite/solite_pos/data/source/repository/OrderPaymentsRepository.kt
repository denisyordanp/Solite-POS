package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment

interface OrderPaymentsRepository : SyncRepository<OrderPayment> {
    suspend fun getNeedUploadOrderPayments(): List<OrderPayment>
    suspend fun insertNewPaymentOrder(payment: OrderPayment)
    suspend fun migrateToUUID()
}
