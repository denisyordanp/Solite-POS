package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.NewOutcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome
import com.socialite.solite_pos.data.source.repository.OutcomesRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import kotlinx.coroutines.flow.first

class NewOutcomeImpl(
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
