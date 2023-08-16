package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.helper.ProductOrderDetail
import com.socialite.solite_pos.data.schema.room.new_master.Customer

fun interface NewOrder {
    suspend operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    )
}
