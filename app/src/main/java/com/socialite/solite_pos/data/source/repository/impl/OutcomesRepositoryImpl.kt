package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.room.OutcomesDao
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

class OutcomesRepositoryImpl(
    private val dao: OutcomesDao,
    private val settingRepository: SettingRepository
) : OutcomesRepository {

    companion object {
        @Volatile
        private var INSTANCE: OutcomesRepositoryImpl? = null

        fun getInstance(
            dao: OutcomesDao,
            settingRepository: SettingRepository
        ): OutcomesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OutcomesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OutcomesRepositoryImpl(
                            dao = dao,
                            settingRepository = settingRepository
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOutcomes(date: String) = dao.getOutcome(date)

    @FlowPreview
    override fun getOutcomes(parameters: ReportsParameter): Flow<List<Outcome>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getSelectedStore().flatMapConcat {
                dao.getOutcome(parameters.start, parameters.end, it)
            }
        } else {
            dao.getOutcome(parameters.start, parameters.end, parameters.storeId)
        }
    }

    override suspend fun insertOutcome(data: Outcome) {
        dao.insertOutcome(data)
    }

    override suspend fun updateOutcome(data: Outcome) {
        dao.updateOutcome(data)
    }
}
