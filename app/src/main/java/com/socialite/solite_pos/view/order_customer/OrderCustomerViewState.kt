package com.socialite.solite_pos.view.order_customer

import com.socialite.solite_pos.data.source.local.entity.helper.MenuBadge
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category

data class OrderCustomerViewState(
    val isShouldSelectStore: Boolean,
    val allProducts: Map<Category, List<ProductWithCategory>>,
    val bucketOrderViewState: BucketOrderViewState,
    val badges: List<MenuBadge>,
) {
    companion object {
        fun idle() = OrderCustomerViewState(
            isShouldSelectStore = false,
            allProducts = emptyMap(),
            bucketOrderViewState = BucketOrderViewState.idle(),
            badges = emptyList()
        )
    }
}
