package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Customer
import kotlinx.coroutines.flow.Flow

fun interface GetCustomers {
    operator fun invoke(): Flow<List<Customer>>
}