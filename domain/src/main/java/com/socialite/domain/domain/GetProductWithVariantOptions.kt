package com.socialite.domain.domain

import com.socialite.schema.ui.helper.VariantWithOptions
import com.socialite.schema.ui.main.Product
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithVariantOptions {
    operator fun invoke(productId: String): Flow<Pair<Product, List<VariantWithOptions>?>>
}
