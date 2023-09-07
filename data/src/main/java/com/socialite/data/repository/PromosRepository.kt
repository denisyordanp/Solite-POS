package com.socialite.data.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.data.schema.room.new_master.Promo
import kotlinx.coroutines.flow.Flow

interface PromosRepository : SyncRepository<Promo> {
    suspend fun insertPromo(data: Promo)
    suspend fun updatePromo(data: Promo)
    fun getPromos(query: SupportSQLiteQuery): Flow<List<Promo>>
    suspend fun migrateToUUID()
    suspend fun deleteAllOldCustomers()
    suspend fun deleteAllNewCustomers()
    suspend fun getNeedUploadPromos(): List<Promo>
}
