package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.local.entity.helper.ProductVariantCount
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductVariantCount {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>>
}
