package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Customer
import com.socialite.domain.schema.helper.ProductOrderDetail

fun interface NewOrder {
    suspend operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    )
}
