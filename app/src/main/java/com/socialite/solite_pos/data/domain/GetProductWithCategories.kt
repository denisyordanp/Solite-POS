package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import kotlinx.coroutines.flow.Flow

fun interface GetProductWithCategories {
    operator fun invoke():  Flow<Map<Category, List<ProductWithCategory>>>
}
