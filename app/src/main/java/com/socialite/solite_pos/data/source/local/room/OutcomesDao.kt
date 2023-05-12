package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome as NewOutcome
import kotlinx.coroutines.flow.Flow

@Dao
interface OutcomesDao {

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE date(${NewOutcome.DATE}) = date(:date)")
    fun getOutcome(date: String): Flow<List<NewOutcome>>

    @Query("SELECT * FROM ${Outcome.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadOutcomes(): List<Outcome>

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE ${Store.ID} = :store AND date(${NewOutcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String, store: String): Flow<List<NewOutcome>>

    @Query("SELECT * FROM '${Outcome.DB_NAME}'")
    suspend fun getOutcomes(): List<Outcome>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutcome(data: Outcome): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewOutcome(data: NewOutcome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutcomes(list: List<Outcome>)

    @Update
    suspend fun updateOutcome(data: Outcome)

    @Query("DELETE FROM ${Outcome.DB_NAME}")
    suspend fun deleteAllOldOutcomes()

    @Query("DELETE FROM ${NewOutcome.DB_NAME}")
    suspend fun deleteAllNewOutcomes()
}
