package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import kotlinx.coroutines.flow.Flow

interface OutcomesRepository {

    fun getOutcomes(date: String): Flow<List<Outcome>>
    fun getOutcomes(from: String, until: String, store: Long): Flow<List<Outcome>>
    suspend fun insertOutcome(data: Outcome)
    suspend fun updateOutcome(data: Outcome)
}
