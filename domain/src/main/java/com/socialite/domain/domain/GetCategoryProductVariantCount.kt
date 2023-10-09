package com.socialite.domain.domain

import com.socialite.schema.ui.helper.ProductVariantCount
import com.socialite.schema.ui.main.Category
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductVariantCount {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>>
}
