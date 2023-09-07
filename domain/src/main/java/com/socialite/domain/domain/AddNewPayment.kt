package com.socialite.domain.domain

import com.socialite.domain.schema.main.Payment

fun interface AddNewPayment {
    suspend operator fun invoke(payment: Payment)
}