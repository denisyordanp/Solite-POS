package com.socialite.data.repository

import com.socialite.data.schema.helper.ReportParameter
import com.socialite.data.schema.room.new_master.Outcome
import kotlinx.coroutines.flow.Flow

interface OutcomesRepository : SyncRepository<Outcome> {
    fun getOutcomes(parameters: ReportParameter): Flow<List<Outcome>>
    suspend fun getNeedUploadOutcomes(): List<Outcome>
    suspend fun insertOutcome(data: Outcome)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOutcomes()
    suspend fun deleteAllNewOutcomes()
}