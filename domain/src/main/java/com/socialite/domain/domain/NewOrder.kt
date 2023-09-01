package com.socialite.domain.domain

import com.socialite.domain.schema.ProductOrderDetail
import com.socialite.domain.schema.main.Customer

fun interface NewOrder {
    suspend operator fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    )
}
