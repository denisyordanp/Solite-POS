package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import kotlinx.coroutines.flow.Flow

interface CustomersRepository {

    fun getCustomers(): Flow<List<Customer>>
    suspend fun insertCustomer(data: Customer)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldCustomers()
    suspend fun deleteAllNewCustomers()
    suspend fun getNotUploadedCustomers(): List<Customer>
    suspend fun getNeedUploadCustomers(): List<Customer>
    suspend fun insertCustomers(customers: List<Customer>)
}
