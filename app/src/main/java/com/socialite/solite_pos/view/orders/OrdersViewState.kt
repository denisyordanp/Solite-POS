package com.socialite.solite_pos.view.orders

import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category

data class OrdersViewState(
    val allProducts: Map<Category, List<ProductWithCategory>>,
    val bucketOrder: BucketOrder,
    val defaultTabPage: Int
) {
    companion object {
        fun idle() = OrdersViewState(
            allProducts = emptyMap(),
            bucketOrder = BucketOrder.idle(),
            defaultTabPage = 0
        )
    }
}
