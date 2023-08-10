package com.socialite.solite_pos.view.screens.order_customer

import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder

data class OrderCustomerViewState(
    val isShouldSelectStore: Boolean,
    val bucketOrder: BucketOrder,
) {
    companion object {
        fun idle() = OrderCustomerViewState(
            isShouldSelectStore = false,
            bucketOrder = BucketOrder.idle(),
        )
    }
}
