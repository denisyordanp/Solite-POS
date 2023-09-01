package com.socialite.domain.domain

import com.socialite.domain.schema.Outcome

fun interface NewOutcome {
    suspend operator fun invoke(outcome: Outcome)
}
