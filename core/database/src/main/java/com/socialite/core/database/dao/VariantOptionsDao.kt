package com.socialite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.schema.database.master.VariantOption
import kotlinx.coroutines.flow.Flow
import com.socialite.schema.database.new_master.VariantOption as NewVariantOption

@Dao
interface VariantOptionsDao {

    @Query("SELECT * FROM '${NewVariantOption.DB_NAME}'")
    fun getNewVariantOptionsFlow(): Flow<List<NewVariantOption>>

    @Query("SELECT * FROM '${VariantOption.DB_NAME}'")
    suspend fun getVariantOptions(): List<VariantOption>

    @Query("SELECT * FROM '${NewVariantOption.DB_NAME}'")
    suspend fun getNewVariantOptions(): List<NewVariantOption>

    @Query("SELECT * FROM '${VariantOption.DB_NAME}' WHERE ${VariantOption.ID} = :variantOptionId LIMIT 1")
    suspend fun getVariantOptionById(variantOptionId: Long): VariantOption?

    @Query("SELECT * FROM ${NewVariantOption.DB_NAME} WHERE ${NewVariantOption.UPLOAD} = 0")
    suspend fun getNeedUploadVariantOptions(): List<NewVariantOption>

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
