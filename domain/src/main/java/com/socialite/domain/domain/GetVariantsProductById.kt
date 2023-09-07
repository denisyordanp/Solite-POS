package com.socialite.domain.domain

import com.socialite.domain.schema.main.VariantProduct
import kotlinx.coroutines.flow.Flow

fun interface GetVariantsProductById {
    operator fun invoke(productId: String): Flow<List<VariantProduct>>
}