package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import kotlinx.coroutines.flow.Flow

interface CustomersRepository {

    fun getCustomers(): Flow<List<Customer>>
    suspend fun insertCustomer(data: Customer)
}
