package com.socialite.solite_pos.data.domain

import com.socialite.data.schema.room.new_master.Outcome

fun interface NewOutcome {
    suspend operator fun invoke(outcome: Outcome)
}
