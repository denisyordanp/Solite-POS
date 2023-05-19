package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Outcome as NewOutcome
import kotlinx.coroutines.flow.Flow

@Dao
interface OutcomesDao {

    @Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) = date(:date)")
    fun getOutcome(date: String): Flow<List<Outcome>>

    @Query("SELECT * FROM ${Outcome.DB_NAME} WHERE ${Outcome.STORE} = :store AND date(${Outcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String, store: Long): Flow<List<Outcome>>

    @Query("SELECT * FROM '${Outcome.DB_NAME}'")
    suspend fun getOutcomes(): List<Outcome>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutcome(data: Outcome): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewOutcome(data: NewOutcome)

    @Update
    suspend fun updateOutcome(data: Outcome)
}
