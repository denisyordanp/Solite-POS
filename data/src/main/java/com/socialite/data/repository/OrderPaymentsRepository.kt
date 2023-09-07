package com.socialite.data.repository

import com.socialite.data.schema.room.new_bridge.OrderPayment

interface OrderPaymentsRepository : SyncRepository<OrderPayment> {
    suspend fun getNeedUploadOrderPayments(): List<OrderPayment>
    suspend fun insertNewPaymentOrder(payment: OrderPayment)
    suspend fun migrateToUUID()
}
