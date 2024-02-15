package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.schema.database.master.Outcome
import com.socialite.schema.database.new_master.Store
import kotlinx.coroutines.flow.Flow
import com.socialite.schema.database.new_master.Outcome as NewOutcome

@Dao
interface OutcomesDao {

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE ${NewOutcome.UPLOAD} = 0")
    suspend fun getNeedUploadOutcomes(): List<NewOutcome>

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE ${Store.ID} = :store AND ${NewOutcome.USER} = :userId AND date(${NewOutcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String, store: String, userId: Long): Flow<List<NewOutcome>>

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE ${NewOutcome.USER} = :userId AND date(${NewOutcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String, userId: Long): Flow<List<NewOutcome>>

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE ${Store.ID} = :store AND date(${NewOutcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String, store: String): Flow<List<NewOutcome>>

    @Query("SELECT * FROM ${NewOutcome.DB_NAME} WHERE date(${NewOutcome.DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOutcome(from: String, until: String): Flow<List<NewOutcome>>

    @Query("SELECT * FROM '${Outcome.DB_NAME}'")
    suspend fun getOutcomes(): List<Outcome>

    @Query("SELECT * FROM '${NewOutcome.DB_NAME}'")
    suspend fun getNewOutcomes(): List<NewOutcome>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewOutcome(data: NewOutcome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutcomes(list: List<NewOutcome>)

    @Update
    suspend fun updateOutcome(data: Outcome)

    @Update
    suspend fun updateOutcomes(data: List<NewOutcome>)

    @Query("DELETE FROM ${Outcome.DB_NAME}")
    suspend fun deleteAllOldOutcomes()

    @Query("DELETE FROM ${NewOutcome.DB_NAME}")
    suspend fun deleteAllNewOutcomes()
}
