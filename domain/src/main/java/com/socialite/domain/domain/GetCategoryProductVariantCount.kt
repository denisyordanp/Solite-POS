package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Category
import com.socialite.domain.schema.helper.ProductVariantCount
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductVariantCount {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>>
}
