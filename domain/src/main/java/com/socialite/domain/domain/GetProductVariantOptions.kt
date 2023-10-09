package com.socialite.domain.domain

import com.socialite.schema.ui.helper.VariantWithOptions
import kotlinx.coroutines.flow.Flow

fun interface GetProductVariantOptions {
    operator fun invoke(idProduct: String): Flow<List<VariantWithOptions>?>
}
