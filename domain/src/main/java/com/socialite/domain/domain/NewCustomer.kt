package com.socialite.domain.domain

import com.socialite.schema.ui.main.Customer

fun interface NewCustomer {
    suspend operator fun invoke(customer: Customer)
}