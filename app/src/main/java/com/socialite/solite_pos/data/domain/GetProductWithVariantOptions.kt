package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithVariantOptions {
    operator fun invoke(productId: String): Flow<Pair<Product, List<VariantWithOptions>?>>
}
