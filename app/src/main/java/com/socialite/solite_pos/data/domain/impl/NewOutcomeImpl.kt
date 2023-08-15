package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.NewOutcome
import com.socialite.solite_pos.data.schema.room.new_master.Outcome
import com.socialite.solite_pos.data.repository.OutcomesRepository
import com.socialite.solite_pos.data.repository.SettingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewOutcomeImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val outcomesRepository: OutcomesRepository
) : NewOutcome {
    override suspend fun invoke(outcome: Outcome) {
        val store = settingRepository.getNewSelectedStore().first()
        outcomesRepository.insertOutcome(outcome.copy(
            store = store
        ))
    }
}
