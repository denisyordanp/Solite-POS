package com.socialite.domain.domain

import com.socialite.schema.ui.helper.Outcome

fun interface NewOutcome {
    suspend operator fun invoke(outcome: Outcome)
}
