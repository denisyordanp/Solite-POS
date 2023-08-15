package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.schema.room.master.Customer
import com.socialite.solite_pos.data.schema.room.new_master.Customer as NewCustomer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomersDao {

    @Query("SELECT * FROM ${Customer.DB_NAME}")
    fun getCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM ${NewCustomer.DB_NAME}")
    fun getNewCustomers(): Flow<List<NewCustomer>>

    @Query("SELECT * FROM ${Customer.DB_NAME} WHERE ${Customer.ID} = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): Customer?

    @Query("SELECT * FROM ${NewCustomer.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadCustomers(): List<NewCustomer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCustomer(data: NewCustomer)

    @Update
    suspend fun updateCustomer(data: Customer)

    @Query("DELETE FROM ${Customer.DB_NAME}")
    suspend fun deleteAllOldCustomers()

    @Query("DELETE FROM ${NewCustomer.DB_NAME}")
    suspend fun deleteAllNewCustomers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(list: List<NewCustomer>)

    @Update
    suspend fun updateCustomers(list: List<NewCustomer>)
}
