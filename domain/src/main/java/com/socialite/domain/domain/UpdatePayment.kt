package com.socialite.domain.domain

import com.socialite.schema.ui.main.Payment

fun interface UpdatePayment {
    suspend operator fun invoke(payment: Payment)
}