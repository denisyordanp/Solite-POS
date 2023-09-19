package com.socialite.domain.domain.impl

import com.socialite.domain.domain.NewOutcome
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.Outcome
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewOutcomeImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    private val outcomesRepository: OutcomesRepository
) : NewOutcome {
    override suspend fun invoke(outcome: Outcome) {
        val dataOutcome = if (outcome.isNewOutcome) {
            val store = settingRepository.getNewSelectedStore().first()
            val loggedInUser = userRepository.getLoggedInUser().first()
            outcome.copy(
                store = store,
                user = loggedInUser!!.id.toLong()
            )
        } else outcome
        outcomesRepository.insertOutcome(dataOutcome.toData())
    }
}
