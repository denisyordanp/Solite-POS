package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Order

fun interface UpdateOrder {
    suspend operator fun invoke(order: Order)
}