package com.socialite.domain.domain

import com.socialite.data.schema.room.new_bridge.VariantProduct
import kotlinx.coroutines.flow.Flow

fun interface GetVariantsProductById {
    operator fun invoke(productId: String): Flow<List<VariantProduct>>
}