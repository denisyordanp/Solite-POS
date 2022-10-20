package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Outcome
import kotlinx.coroutines.flow.Flow

@Dao
interface OutcomesDao {

    @Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) = date(:date)")
    fun getOutcome(date: String): Flow<List<Outcome>>

    @Query("SELECT * FROM ${Outcome.DB_NAME} WHERE date(${Outcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String): Flow<List<Outcome>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutcome(data: Outcome): Long

    @Update
    fun updateOutcome(data: Outcome)
}
