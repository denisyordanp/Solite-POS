package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow

interface OutcomesRepository {

    fun getOutcomes(date: String): Flow<List<Outcome>>
    fun getOutcomes(parameters: ReportsParameter): Flow<List<Outcome>>
    suspend fun getNeedUploadOutcomes(): List<Outcome>
    suspend fun insertOutcome(data: Outcome)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOutcomes()
    suspend fun deleteAllNewOutcomes()
    suspend fun insertOutcomes(list: List<Outcome>)
}
