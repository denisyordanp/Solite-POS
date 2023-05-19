package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant as NewVariant
import kotlinx.coroutines.flow.Flow

@Dao
interface VariantsDao {

    @Query("SELECT * FROM ${Variant.DB_NAME}")
    fun getVariants(): Flow<List<Variant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVariant(data: Variant): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVariant(data: NewVariant)

    @Update
    suspend fun updateVariant(data: Variant)
}
