package com.socialite.domain.domain

import com.socialite.domain.schema.ProductWithCategory
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategoryById {
    operator fun invoke(productId: String): Flow<ProductWithCategory?>
}
