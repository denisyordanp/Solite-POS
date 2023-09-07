package com.socialite.domain.domain

import com.socialite.domain.schema.ProductVariantCount
import com.socialite.domain.schema.main.Category
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductVariantCount {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>>
}
