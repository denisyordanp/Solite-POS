package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository {
    suspend fun insertPayment(data: Payment)
    suspend fun updatePayment(data: Payment)
    fun getPayments() : Flow<List<Payment>>
}
