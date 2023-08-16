package com.socialite.solite_pos.data.domain

import com.socialite.solite_pos.data.schema.helper.ProductOrderDetail

fun interface UpdateOrderProducts {
    suspend operator fun invoke(orderId: String, products: List<ProductOrderDetail>)
}
