package com.socialite.solite_pos.data.source.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_master.VariantOption
import kotlinx.coroutines.flow.Flow

interface VariantOptionsRepository {

    fun getVariantOptions(query: SupportSQLiteQuery): Flow<List<VariantOption>>
    fun getVariantsWithOptions(): Flow<List<VariantWithOptions>>
    suspend fun getVariantOptions(): List<VariantOption>
    suspend fun insertVariantOption(data: VariantOption)
    suspend fun updateVariantOption(data: VariantOption)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldVariantOptions()
    suspend fun deleteAllNewVariantOptions()
}
