package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Outcome

fun interface NewOutcome {
    suspend operator fun invoke(outcome: Outcome)
}
