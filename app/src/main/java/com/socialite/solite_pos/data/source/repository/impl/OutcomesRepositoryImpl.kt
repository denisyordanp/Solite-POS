package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.room.OutcomesDao
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.utils.tools.helper.OrdersParameter

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

    override fun getOutcomes(parameters: OrdersParameter) =
        dao.getOutcome(parameters.start, parameters.end, parameters.storeId)

    override suspend fun insertOutcome(data: Outcome) {
        dao.insertOutcome(data)
    }

    override suspend fun updateOutcome(data: Outcome) {
        dao.updateOutcome(data)
    }
}
