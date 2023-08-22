package com.socialite.domain.domain.impl

import com.socialite.domain.domain.NewOutcome
import com.socialite.data.schema.room.new_master.Outcome
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.SettingRepository
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
