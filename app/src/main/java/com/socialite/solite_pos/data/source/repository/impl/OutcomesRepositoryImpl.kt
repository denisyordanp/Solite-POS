package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.room.OutcomesDao
import com.socialite.solite_pos.data.source.repository.OutcomesRepository

class OutcomesRepositoryImpl(
    private val dao: OutcomesDao
) : OutcomesRepository {

    companion object {
        @Volatile
        private var INSTANCE: OutcomesRepositoryImpl? = null

        fun getInstance(
            dao: OutcomesDao
        ): OutcomesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OutcomesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OutcomesRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOutcomes(date: String) = dao.getOutcome(date)

    override fun getOutcomes(from: String, until: String, store: Long) =
        dao.getOutcome(from, until, store)

    override suspend fun insertOutcome(data: Outcome) {
        dao.insertOutcome(data)
    }

    override suspend fun updateOutcome(data: Outcome) {
        dao.updateOutcome(data)
    }
}
