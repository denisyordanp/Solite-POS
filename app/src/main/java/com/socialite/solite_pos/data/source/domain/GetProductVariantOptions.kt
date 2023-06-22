package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetProductVariantOptions {
    operator fun invoke(idProduct: String): Flow<List<VariantWithOptions>?>
}
