package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomersDao {

    @Query("SELECT * FROM ${Customer.DB_NAME}")
    fun getCustomers(): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(data: Customer): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCustomer(data: Customer)
}
