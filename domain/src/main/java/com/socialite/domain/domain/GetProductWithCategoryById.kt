package com.socialite.domain.domain

import com.socialite.schema.ui.helper.ProductWithCategory
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategoryById {
    operator fun invoke(productId: String): Flow<ProductWithCategory?>
}
