package com.socialite.data.repository

import com.socialite.data.schema.room.new_master.Outcome
import kotlinx.coroutines.flow.Flow

interface OutcomesRepository : SyncRepository<Outcome> {
    fun getOutcomes(from: String, until: String, store: String, userId: Long): Flow<List<Outcome>>
    fun getOutcomesAllStore(from: String, until: String, userId: Long): Flow<List<Outcome>>
    fun getOutcomesAllUser(from: String, until: String, store: String): Flow<List<Outcome>>
    fun getOutcomesAllStoreAndUser(from: String, until: String): Flow<List<Outcome>>
    suspend fun getNeedUploadOutcomes(): List<Outcome>
    suspend fun insertOutcome(data: Outcome)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOutcomes()
    suspend fun deleteAllNewOutcomes()
}
