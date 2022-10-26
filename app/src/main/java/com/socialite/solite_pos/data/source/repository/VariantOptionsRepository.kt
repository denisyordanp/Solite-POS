package com.socialite.solite_pos.data.source.repository

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import kotlinx.coroutines.flow.Flow

interface VariantOptionsRepository {

    fun getVariantOptions(query: SupportSQLiteQuery): Flow<List<VariantOption>>
    suspend fun insertVariantOption(data: VariantOption)
    suspend fun updateVariantOption(data: VariantOption)
}