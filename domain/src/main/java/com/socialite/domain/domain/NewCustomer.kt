package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Customer

fun interface NewCustomer {
    suspend operator fun invoke(customer: Customer)
}