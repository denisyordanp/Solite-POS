package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.schema.room.new_master.Outcome
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.flow.Flow

interface OutcomesRepository : SyncRepository<Outcome> {
    fun getOutcomes(parameters: ReportParameter): Flow<List<Outcome>>
    suspend fun getNeedUploadOutcomes(): List<Outcome>
    suspend fun insertOutcome(data: Outcome)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOutcomes()
    suspend fun deleteAllNewOutcomes()
}
