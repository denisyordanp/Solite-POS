package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment as NewPayment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentsDao {

    @RawQuery(observedEntities = [Payment::class])
    fun getPayments(query: SupportSQLiteQuery): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(data: Payment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPayment(data: NewPayment)

    @Update
    suspend fun updatePayment(data: Payment)
}
