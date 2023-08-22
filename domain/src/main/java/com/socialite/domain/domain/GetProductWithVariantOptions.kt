package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Product
import com.socialite.domain.schema.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithVariantOptions {
    operator fun invoke(productId: String): Flow<Pair<Product, List<VariantWithOptions>?>>
}
