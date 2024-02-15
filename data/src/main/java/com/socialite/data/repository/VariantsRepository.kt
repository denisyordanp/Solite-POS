package com.socialite.data.repository

import com.socialite.schema.database.new_master.Variant
import kotlinx.coroutines.flow.Flow

interface VariantsRepository : SyncRepository<Variant> {

    fun getVariants(): Flow<List<Variant>>
    suspend fun getNeedUploadVariants(): List<Variant>
    suspend fun insertVariant(data: Variant)
    suspend fun updateVariant(data: Variant)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldVariants()
    suspend fun deleteAllNewVariants()
}
