package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.schema.room.master.Payment
import com.socialite.solite_pos.data.schema.room.new_master.Payment as NewPayment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentsDao {

    @RawQuery(observedEntities = [Payment::class])
    fun getPayments(query: SupportSQLiteQuery): Flow<List<Payment>>

    @RawQuery(observedEntities = [NewPayment::class])
    fun getNewPayments(query: SupportSQLiteQuery): Flow<List<NewPayment>>

    @Query("SELECT * FROM ${NewPayment.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadPayments(): List<NewPayment>

    @Query("SELECT * FROM '${Payment.DB_NAME}' WHERE ${Payment.ID} = :paymentId LIMIT 1")
    suspend fun getPaymentById(paymentId: Long): Payment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPayment(data: NewPayment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(list: List<NewPayment>)

    @Update
    suspend fun updatePayment(data: Payment)

    @Update
    suspend fun updateNewPayment(data: NewPayment)

    @Update
    suspend fun updatePayments(data: List<NewPayment>)

    @Query("DELETE FROM '${Payment.DB_NAME}'")
    suspend fun deleteAllOldPayments()

    @Query("DELETE FROM '${NewPayment.DB_NAME}'")
    suspend fun deleteAllNewPayments()
}
