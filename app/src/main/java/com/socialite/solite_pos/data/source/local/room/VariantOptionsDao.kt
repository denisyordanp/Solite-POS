package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import kotlinx.coroutines.flow.Flow

@Dao
interface VariantOptionsDao {

    @RawQuery(observedEntities = [VariantOption::class])
    fun getVariantOptions(query: SupportSQLiteQuery): Flow<List<VariantOption>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariantOption(data: VariantOption): Long

    @Update
    fun updateVariantOption(data: VariantOption)
}
