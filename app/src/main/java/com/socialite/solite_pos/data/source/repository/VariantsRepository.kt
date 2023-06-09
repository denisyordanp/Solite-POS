package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Variant
import kotlinx.coroutines.flow.Flow

interface VariantsRepository : SyncRepository<Variant> {

    fun getVariants(): Flow<List<Variant>>
    suspend fun getNeedUploadVariants(): List<Variant>
    suspend fun insertVariant(data: Variant)
    suspend fun insertVariants(list: List<Variant>)
    suspend fun updateVariant(data: Variant)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldVariants()
    suspend fun deleteAllNewVariants()
}
