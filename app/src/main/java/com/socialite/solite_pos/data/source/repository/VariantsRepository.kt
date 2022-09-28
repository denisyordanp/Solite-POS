package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Variant
import kotlinx.coroutines.flow.Flow

interface VariantsRepository {

    fun getVariants(): Flow<List<Variant>>
    suspend fun insertVariant(data: Variant)
    suspend fun updateVariant(data: Variant)
}
