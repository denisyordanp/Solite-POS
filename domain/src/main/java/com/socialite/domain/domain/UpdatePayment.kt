package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Payment

fun interface UpdatePayment {
    suspend operator fun invoke(payment: Payment)
}