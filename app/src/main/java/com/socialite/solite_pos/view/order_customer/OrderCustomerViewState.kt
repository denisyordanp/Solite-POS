package com.socialite.solite_pos.view.order_customer

import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.GeneralMenuBadge
import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category

data class OrderCustomerViewState(
    val isShouldSelectStore: Boolean,
    val allProducts: Map<Category, List<ProductWithCategory>>,
    val bucketOrder: BucketOrder,
    val badges: List<GeneralMenuBadge>,
) {
    companion object {
        fun idle() = OrderCustomerViewState(
            isShouldSelectStore = false,
            allProducts = emptyMap(),
            bucketOrder = BucketOrder.idle(),
            badges = emptyList()
        )
    }
}
