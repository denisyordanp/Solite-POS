package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment

interface OrderPaymentsRepository {
    suspend fun getNeedUploadOrderPayments(): List<OrderPayment>
    suspend fun insertOrderPayments(list: List<OrderPayment>)
    suspend fun insertNewPaymentOrder(payment: OrderPayment)
    suspend fun migrateToUUID()
}
