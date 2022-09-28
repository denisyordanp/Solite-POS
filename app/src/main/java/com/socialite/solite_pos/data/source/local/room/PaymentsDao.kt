package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentsDao {

    @Query("SELECT * FROM ${Payment.DB_NAME}")
    fun getPayments(): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(data: Payment)

    @Update
    fun updatePayment(data: Payment)
}
