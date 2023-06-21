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

    @Query("SELECT * FROM ${NewVariant.DB_NAME}")
    fun getNewVariants(): Flow<List<NewVariant>>

    @Query("SELECT * FROM ${Variant.DB_NAME} WHERE ${Variant.ID} = :variantId")
    suspend fun getVariantById(variantId: Long): Variant?

    @Query("SELECT * FROM ${NewVariant.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadVariants(): List<NewVariant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVariant(data: NewVariant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(list: List<NewVariant>)

    @Update
    suspend fun updateVariant(data: Variant)

    @Update
    suspend fun updateNewVariant(data: NewVariant)

    @Update
    suspend fun updateVariants(data: List<NewVariant>)

    @Query("DELETE FROM ${Variant.DB_NAME}")
    suspend fun deleteAllOldVariants()

    @Query("DELETE FROM ${NewVariant.DB_NAME}")
    suspend fun deleteAllNewVariants()
}
