package com.socialite.domain.domain

import com.socialite.domain.schema.main.Order

fun interface UpdateOrder {
    suspend operator fun invoke(order: Order)
}