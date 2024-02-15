package com.socialite.domain.domain

import com.socialite.schema.ui.helper.ProductOrderDetail

fun interface UpdateOrderProducts {
    suspend operator fun invoke(orderId: String, products: List<ProductOrderDetail>)
}
