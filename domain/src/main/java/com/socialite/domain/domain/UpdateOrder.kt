package com.socialite.domain.domain

import com.socialite.schema.ui.main.Order

fun interface UpdateOrder {
    suspend operator fun invoke(order: Order)
}