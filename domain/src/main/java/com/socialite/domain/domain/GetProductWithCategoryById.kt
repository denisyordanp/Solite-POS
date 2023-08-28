package com.socialite.domain.domain

import com.socialite.data.schema.room.helper.ProductWithCategory
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategoryById {
    operator fun invoke(productId: String): Flow<ProductWithCategory?>
}
