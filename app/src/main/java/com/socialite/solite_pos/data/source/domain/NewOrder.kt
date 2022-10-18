package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer

interface NewOrder {
    suspend operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    )
}
