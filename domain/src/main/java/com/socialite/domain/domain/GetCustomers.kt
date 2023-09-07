package com.socialite.domain.domain

import com.socialite.domain.schema.main.Customer
import kotlinx.coroutines.flow.Flow

fun interface GetCustomers {
    operator fun invoke(): Flow<List<Customer>>
}