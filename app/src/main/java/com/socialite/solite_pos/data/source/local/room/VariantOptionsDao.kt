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
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption as NewVariantOption
import kotlinx.coroutines.flow.Flow

@Dao
interface VariantOptionsDao {

    @RawQuery(observedEntities = [NewVariantOption::class])
    fun getVariantOptions(query: SupportSQLiteQuery): Flow<List<NewVariantOption>>

    @Query("SELECT * FROM '${VariantOption.DB_NAME}'")
    suspend fun getVariantOptions(): List<VariantOption>

    @Query("SELECT * FROM '${NewVariantOption.DB_NAME}'")
    suspend fun getNewVariantOptions(): List<NewVariantOption>

    @Query("SELECT * FROM '${VariantOption.DB_NAME}' WHERE ${VariantOption.ID} = :variantOptionId LIMIT 1")
    suspend fun getVariantOptionById(variantOptionId: Long): VariantOption?

    @Query("SELECT * FROM ${NewVariantOption.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadVariantOptions(): List<NewVariantOption>

    @Transaction
    @Query("SELECT * FROM ${NewVariantOption.DB_NAME}")
    fun getVariantWithOptions(): Flow<List<VariantWithOption>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantOption(data: VariantOption)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewVariantOption(data: NewVariantOption)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariantOptions(list: List<NewVariantOption>)

    @Update
    suspend fun updateVariantOption(data: VariantOption)

    @Update
    suspend fun updateNewVariantOption(data: NewVariantOption)

    @Update
    suspend fun updateVariantOptions(data: List<NewVariantOption>)

    @Query("DELETE FROM ${VariantOption.DB_NAME}")
    suspend fun deleteAllOldVariantOptions()

    @Query("DELETE FROM ${NewVariantOption.DB_NAME}")
    suspend fun deleteAllNewVariantOptions()
}
