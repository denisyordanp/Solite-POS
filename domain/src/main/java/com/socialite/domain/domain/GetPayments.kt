package com.socialite.domain.domain

import com.socialite.schema.ui.main.Payment
import kotlinx.coroutines.flow.Flow

fun interface GetPayments {
    operator fun invoke(status: Payment.Status): Flow<List<Payment>>
}