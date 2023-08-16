package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetProductVariantOptions {
    operator fun invoke(idProduct: String): Flow<List<VariantWithOptions>?>
}
