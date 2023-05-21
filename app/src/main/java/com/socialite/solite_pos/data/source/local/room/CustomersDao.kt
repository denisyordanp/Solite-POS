package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer as NewCustomer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomersDao {

    @Query("SELECT * FROM ${Customer.DB_NAME}")
    fun getCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM ${NewCustomer.DB_NAME}")
    fun getNewCustomers(): Flow<List<NewCustomer>>

    @Query("SELECT * FROM ${Customer.DB_NAME} WHERE ${Customer.ID} = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(data: Customer): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCustomer(data: NewCustomer)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCustomer(data: Customer)

    @Query("DELETE FROM ${Customer.DB_NAME}")
    suspend fun deleteAllOldCustomers()
}
