package com.socialite.domain.domain

import com.socialite.domain.schema.main.Customer

fun interface NewCustomer {
    suspend operator fun invoke(customer: Customer)
}