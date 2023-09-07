package com.socialite.domain.domain

import com.socialite.domain.schema.ProductOrderDetail

fun interface UpdateOrderProducts {
    suspend operator fun invoke(orderId: String, products: List<ProductOrderDetail>)
}
