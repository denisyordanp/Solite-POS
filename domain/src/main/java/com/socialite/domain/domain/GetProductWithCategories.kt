package com.socialite.domain.domain

import com.socialite.domain.schema.ProductWithCategory
import com.socialite.domain.schema.main.Category
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategories {
    operator fun invoke():  Flow<Map<Category, List<ProductWithCategory>>>
}
