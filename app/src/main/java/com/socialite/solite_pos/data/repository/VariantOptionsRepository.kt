package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import kotlinx.coroutines.flow.Flow

interface VariantOptionsRepository : SyncRepository<VariantOption> {

    fun getVariantOptions(): Flow<List<VariantOption>>
    suspend fun getNeedUploadVariantOptions(): List<VariantOption>
    suspend fun insertVariantOption(data: VariantOption)
    suspend fun updateVariantOption(data: VariantOption)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldVariantOptions()
    suspend fun deleteAllNewVariantOptions()
}
