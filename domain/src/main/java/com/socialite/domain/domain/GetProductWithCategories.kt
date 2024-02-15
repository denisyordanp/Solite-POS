package com.socialite.domain.domain

import com.socialite.schema.ui.helper.ProductWithCategory
import com.socialite.schema.ui.main.Category
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategories {
    operator fun invoke():  Flow<Map<Category, List<ProductWithCategory>>>
}
