package com.socialite.solite_pos.view.screens.orders

import com.socialite.domain.schema.BucketOrder

data class OrdersViewState(
    val bucketOrder: BucketOrder,
    val defaultTabPage: Int
) {
    companion object {
        fun idle() = OrdersViewState(
            bucketOrder = BucketOrder.idle(),
            defaultTabPage = 0
        )
    }
}
