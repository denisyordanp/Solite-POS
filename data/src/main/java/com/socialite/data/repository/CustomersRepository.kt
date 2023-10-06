package com.socialite.data.repository

import com.socialite.schema.database.new_master.Customer
import kotlinx.coroutines.flow.Flow

interface CustomersRepository : SyncRepository<Customer> {

    fun getCustomers(): Flow<List<Customer>>
    suspend fun insertCustomer(data: Customer)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldCustomers()
    suspend fun deleteAllNewCustomers()
    suspend fun getNeedUploadCustomers(): List<Customer>
}
