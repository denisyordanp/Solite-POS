package com.socialite.domain.domain

import com.socialite.domain.schema.VariantWithOptions
import com.socialite.domain.schema.main.Product
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithVariantOptions {
    operator fun invoke(productId: String): Flow<Pair<Product, List<VariantWithOptions>?>>
}
