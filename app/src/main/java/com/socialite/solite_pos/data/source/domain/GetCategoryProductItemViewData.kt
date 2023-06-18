package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.view.store.product_master.ProductItemViewData
import kotlinx.coroutines.flow.Flow

fun interface GetCategoryProductItemViewData {
    operator fun invoke(): Flow<List<Pair<Category, List<ProductItemViewData>>>>
}
