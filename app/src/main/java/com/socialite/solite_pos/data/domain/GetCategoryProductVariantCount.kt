package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.room.new_master.Category
import com.socialite.solite_pos.data.schema.helper.ProductVariantCount
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductVariantCount {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>>
}
