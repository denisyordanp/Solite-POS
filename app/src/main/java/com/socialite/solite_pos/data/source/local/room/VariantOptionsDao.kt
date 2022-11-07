package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantWithOption
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import kotlinx.coroutines.flow.Flow

@Dao
interface VariantOptionsDao {

    @RawQuery(observedEntities = [VariantOption::class])
    fun getVariantOptions(query: SupportSQLiteQuery): Flow<List<VariantOption>>

    @Transaction
    @Query("SELECT * FROM ${VariantOption.DB_NAME}")
    fun getVariantWithOptions(): Flow<List<VariantWithOption>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariantOption(data: VariantOption)

    @Update
    fun updateVariantOption(data: VariantOption)
}
