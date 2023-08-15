package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.schema.room.bridge.VariantMix
import com.socialite.solite_pos.data.schema.room.helper.VariantWithVariantMix
import kotlinx.coroutines.flow.Flow

interface VariantMixesRepository {

    fun getVariantMixProductById(idVariant: Long, idProduct: Long): Flow<VariantMix?>
    fun getVariantMixProduct(idVariant: Long): Flow<VariantWithVariantMix>
    suspend fun insertVariantMix(data: VariantMix)
    suspend fun removeVariantMix(data: VariantMix)
}
