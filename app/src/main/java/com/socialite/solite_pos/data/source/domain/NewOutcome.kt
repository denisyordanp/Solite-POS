package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome

interface NewOutcome {
    suspend operator fun invoke(outcome: Outcome)
}
