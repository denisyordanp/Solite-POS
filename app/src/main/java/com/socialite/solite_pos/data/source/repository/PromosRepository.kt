package com.socialite.solite_pos.data.source.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import kotlinx.coroutines.flow.Flow

interface PromosRepository {
    suspend fun insertPromo(data: Promo)
    suspend fun insertPromos(list: List<Promo>)
    suspend fun updatePromo(data: Promo)
    fun getPromos(query: SupportSQLiteQuery): Flow<List<Promo>>
    suspend fun migrateToUUID()
    suspend fun deleteAllOldCustomers()
    suspend fun deleteAllNewCustomers()
    suspend fun getNeedUploadPromos(): List<Promo>
}
