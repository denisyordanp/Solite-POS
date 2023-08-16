package com.socialite.solite_pos.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.schema.room.new_master.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentsRepository : SyncRepository<Payment> {
    suspend fun insertPayment(data: Payment)
    suspend fun updatePayment(data: Payment)
    fun getPayments(query: SupportSQLiteQuery) : Flow<List<Payment>>
    suspend fun migrateToUUID()
    suspend fun deleteAllOldPayments()
    suspend fun deleteAllNewPayments()
    suspend fun getNeedUploadPayments() : List<Payment>
}
